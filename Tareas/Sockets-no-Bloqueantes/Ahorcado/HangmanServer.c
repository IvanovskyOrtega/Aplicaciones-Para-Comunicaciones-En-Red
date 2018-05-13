#include <fcntl.h>
#include <stdio.h>      //
#include <string.h>     //
#include <unistd.h>     //
#include <netinet/in.h> //
#include <sys/types.h>  //
#include <sys/socket.h> //
#include <stdlib.h>     //exit
#include <netdb.h>      //getaddrinfo() getnameinfo() freeaddrinfo()
#include <time.h>

#define port "7"
#define MAXBUF 1024
#define MAXATTEMPTS 5

char * YOUWIN =  "\nYOU WIN";
char * YOULOSE = "\nYOU LOSE";

void error(const char *msj)
{
    perror(msj);
    exit(1);
} //error

typedef struct CLIENT
{
    int fd;
    struct sockaddr_storage addr;
    int difficult;
    int guessed;
    int len;
    int attempts;
    char* res;
    char* stringToGuess;
} CLIENT;

char* easy[20] = {
    "hola","pera","roca","liga","mapa",
    "foco","jale","vaca","moco","jugo",
    "liso","palo","puro","vaso","buzo",
    "beso","lazo","asco","malo","mano"
};

char* intermediate[20] = {
    "babosos","abismos","cocinas","demoras","escoger",
    "figuras","hundido","interes","jirafas","kimonos",
    "lastima","mostrar","navegar","oprimir","perdido",
    "quemame","rotarlo","saborea","tiempos","unifica"
};

char* hard[20] = {
    "acomodarse","multiplica","negociando","aerolineas","babilonios",
    "becerrillo","cabalgaste","dactilares","ejecutable","gilipollas",
    "idealmente","jubilacion","laborables","marcadores","obedeceria",
    "quebrantar","rabadillas","sabatismos","tabaqueras","ucranianas"
};

int createServerSocket(struct addrinfo *p);
char* getString(int difficult);
void initializeGameParameters(char *buf, CLIENT *client, int sockfd);
void lookForCoincidences(CLIENT *client, char c, int sockfd);
void closeClientConnection(int sockfd, CLIENT *clients, fd_set *a);

int main(int argc, char **argv)
{

    int sd, cd, n, n1, v = 1, rv, op = 0, i, nready, maxi = -1, sockfd, maxfd = -1;
    socklen_t ctam;
    char s[INET6_ADDRSTRLEN], hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];
    struct addrinfo hints, *servinfo, *p;
    struct sockaddr_storage cdir; // connector's address
    ctam = sizeof(cdir);
    struct timeval tv;
    fd_set a1, a;
    char buf[MAXBUF + 1];
    CLIENT client[FD_SETSIZE];
    memset(&hints, 0, sizeof(hints)); //indicio
    hints.ai_family = AF_INET6;       /* Allow IPv4 or IPv6  familia de dir*/
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    hints.ai_protocol = 0;       /* Any protocol */
    hints.ai_canonname = NULL;
    hints.ai_addr = NULL;
    hints.ai_next = NULL;

    if ((rv = getaddrinfo(NULL, port, &hints, &servinfo)) != 0)
    {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    } //if

    for (p = servinfo; p != NULL; p = p->ai_next)
    {
        if ((sd = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1)
        {
            perror("server: socket");
            continue;
        } //if

        if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &v, sizeof(int)) == -1)
        {
            perror("setsockopt");
            exit(1);
        } //if

        if (setsockopt(sd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1)
        {
            perror("IPv6 not supported");
            exit(1);
        } //if

        int flags = fcntl(sd, F_GETFL, 0);
        fcntl(sd, F_SETFL, flags | O_NONBLOCK);

        if (bind(sd, p->ai_addr, p->ai_addrlen) == -1)
        {
            close(sd);
            perror("bind() error");
            continue;
        } //if

        break;
    }                       //for
    freeaddrinfo(servinfo); // all done with this structure
    if (p == NULL)
    {
        fprintf(stderr, "server: bind() error\n");
        exit(1);
    } //if

    if (listen(sd, FD_SETSIZE) == -1)
    {
        perror("listen() error");
        exit(1);
    } //if
    for (i = 0; i < FD_SETSIZE; i++)
    {
        client[i].fd = -1;
        client[i].difficult = 0;
        client[i].guessed = 0;
        client[i].attempts = 0;
    } //for

    FD_ZERO(&a);
    FD_SET(sd, &a);
    maxfd = sd;
    printf("Server ready to atend a maximum of %d connections...\n", FD_SETSIZE);
    srand(time(0));
    while (1)
    {
        a1 = a;
        tv.tv_sec = 1;
        tv.tv_usec = 0;
        nready = select(maxfd + 1, &a1, NULL, NULL, &tv);
        if (nready == 0)
            continue;
        else if (nready < 0)
        {
            printf("select() error\n");
            break;
        }
        else
        {
            if (FD_ISSET(sd, &a1)) // nueva conexion
            {
                if ((cd = accept(sd, (struct sockaddr *)&cdir, &ctam)) == -1)
                {
                    perror("accept() error\n");
                    continue;
                } //if
                for (i = 0; i < FD_SETSIZE; i++)
                {
                    if (client[i].fd < 0)
                    {
                        client[i].fd = cd;
                        client[i].addr = cdir;
                        if (getnameinfo((struct sockaddr *)&cdir, sizeof(cdir), hbuf, sizeof(hbuf), sbuf, sizeof(sbuf), NI_NUMERICHOST | NI_NUMERICSERV) == 0)
                            printf("A client has connected from %s:%s\n", hbuf, sbuf);
                        break;
                    } //if
                }     //for
                if (i == FD_SETSIZE)
                    printf("Too many connections");
                FD_SET(cd, &a);
                if (cd > maxfd)
                    maxfd = cd;
                if (i > maxi)
                    maxi = i;
            }
            else
            {
                for (i = 0; i <= maxi; i++)
                {
                    if ((sockfd = client[i].fd) < 0)
                        continue;
                    if (FD_ISSET(sockfd, &a1))
                    {
                        bzero(buf, MAXBUF + 1);
                        if ((n = recv(sockfd, buf, MAXBUF, 0)) > 0)
                        {
                            printf("Message from client %s: %s\n", hbuf, buf);
                            if(client[i].difficult == 0){
                                initializeGameParameters(buf,&client[i],sockfd);
                            }
                            else{
                                lookForCoincidences(&client[i], buf[0], sockfd);
                                if(client[i].guessed == client[i].len){
                                    send(sockfd, YOUWIN, strlen(YOUWIN), 0);
                                    goto close;
                                }
                                else if(client[i].attempts == MAXATTEMPTS-1)
                                {
                                    send(sockfd, YOULOSE, strlen(YOULOSE), 0);
                                    goto close;
                                }
                            }
                        }
                        else
                        {
                            close:
                            FD_CLR(sockfd, &a);
                            closeClientConnection(sockfd, &client[i], &a);
                        } //else
                    }     //if
                }         //for
            }             //else
        }
    } //while
    close(sd);
} //main

void closeClientConnection(int sockfd, CLIENT *client, fd_set *a){
    printf("Connection closed by client\n");
    close(sockfd);
    client->fd = -1;
    client->difficult = 0;
    client->guessed = 0;
    client->attempts = 0;
}

void initializeGameParameters(char *buf, CLIENT *client, int sockfd){
    int n;
    int difficult = atoi(buf);
    client->difficult = difficult;
    char *stringToGuess;
    char * str = getString(difficult);
    printf("The client must guess the string: %s\n",str);
    client->stringToGuess = str;
    switch(difficult){
        case 1: client->res = "____"; client->len = 4; stringToGuess = "____\n"; break;
        case 2: client->res = "_______"; client->len = 7; stringToGuess = "_______\n"; break;
        case 3: client->res = "__________"; client->len = 10; stringToGuess = "__________\n"; break;
        default: client->res = "____"; client->len = 4; stringToGuess = "____\n"; break;
    }
    n = send(sockfd, stringToGuess, strlen(stringToGuess), 0);
}

char* getString(int difficult){
    int rand_pos = rand()%20;
    char* cad;
    switch(difficult){
        case 1:
        return easy[rand_pos];
        case 2:
        return intermediate[rand_pos];
        case 3:
        return hard[rand_pos];
        default:
        return "xddd";
    }
}

void lookForCoincidences(CLIENT *client, char c, int sockfd){
    int i;
    int guessed = 0;
    int len = client->len;
    char attempts[21];
    char* resAux = (char *)malloc((len+1)*sizeof(char));
    for(i = 0; i < len; i++){
        if(client->res[i] == '_'){
        if(client->stringToGuess[i] == c){
            resAux[i] = c;
            guessed++;
        }
        else
            resAux[i] = '_';
        }
        else{
        resAux[i] = client->res[i];
        }
    }
    if(guessed == 0) client->attempts += 1;
    sprintf(attempts,"You have %d attempts\n",5-client->attempts);
    int n = send(sockfd, attempts, strlen(attempts), 0);
    resAux[len+1] = '\n';
    client->guessed += guessed;
    client->res = resAux;
    n = send(sockfd, resAux, strlen(resAux), 0);
}
