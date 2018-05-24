from ply.yacc import yacc
from ply.lex import lex
from sql_parser import parser,attributes,types,classnameL
from sql_lexer import lexer
import sys
import os

def generate_intermediate_code(database_name):
    
    # Get the class and file names
    classname = classnameL[0]
    filename = 'src/src/GeneratedClasses/'+database_name+'/'+classname+'.java'

    # Create a public class implementing the Serializable class
    java_class = 'package '+database_name+';\n\n'
    java_class += 'import java.io.Serializable;\n\n'
    java_class += 'public class '+classname+'  implements Serializable{\n'
    java_class += ''

    # Get the number of attributes for the class
    attrib_num = len(attributes)

    # Define them as attributes of the class
    j = attrib_num - 1
    for i in range(0,attrib_num):
        java_class += '\tpublic '+types[i]+' '+attributes[j]+';\n'
        j -= 1

    java_class = addGetters(java_class,attrib_num)
    java_class = addSetters(java_class,attrib_num)
    java_class = addConstructor(classname,java_class,attrib_num)

    # End of the class
    java_class += '}'

    directory = 'src/src/GeneratedClasses/'+database_name+'/'
    filename = directory+classname+'.java'

    if not os.path.exists(directory):
        os.makedirs(directory)

    # Write a .java file the generated class for later compilation
    with open(filename,'w') as intermediate_code:
        intermediate_code.write(java_class)

    # Uncommet to see the generated file
    #print(java_class)

def addGetters(java_class,attrib_num):

    # Iterate by each attribute
    j = attrib_num - 1
    for i in range(0,attrib_num):
        java_class += '\tpublic '+types[i]+' get_'+attributes[j]+'(){\n'
        java_class += '\t\treturn this.'+attributes[j]+';\n'
        java_class += '\t}\n'
        j -= 1

    return java_class

def addSetters(java_class,attrib_num):

    # Iterate by each attribute
    j = attrib_num - 1
    for i in range(0,attrib_num):
        java_class += '\tpublic void set_'+attributes[j]+'('+types[i]+' '+attributes[j]+'){\n'
        java_class += '\t\tthis.'+attributes[j]+' = '+attributes[j]+';\n'
        java_class += '\t}\n'
        j -= 1

    return java_class

def addConstructor(classname,java_class,attrib_num):

    # Empty constructor
    java_class += '\tpublic '+classname+'(){\n}\n'

    # Constructor with parameters
    java_class += '\tpublic '+classname+'('

    # Iterate by each attribute
    j = attrib_num - 1
    
    for i in range(0,attrib_num-1):
        java_class += types[i]+' '+attributes[j]+', '
        j -= 1

    java_class += types[attrib_num-1]+' '+attributes[0]+'){\n'

    j = attrib_num - 1
    for i in range(0,attrib_num):
        java_class += '\t\tthis.'+attributes[j]+' = '+attributes[j]+';\n'
        j -= 1

    java_class += '\t}\n'

    return java_class

class SQLParser:

    def __init__(self):
        self.lexer = lexer
        self.parser = parser

    def analyzeFile(self,filename):
        with open(filename,'r') as f:
            input = f.read()
            # print(input)
            parser.parse(input,lexer=lexer)

    def analyzeString(self,input):
        parser.parse(input,lexer=lexer)

class Main:

    def startAnalysis(self,option,database_name):
        sqlParser = SQLParser()
        if option == 1:
            try:
                sqlParser.analyzeFile(str(sys.argv[1]))
            except:
                print('Try a valid file format')
        elif option == 0:
            if database_name != '.' and database_name != 'null' :
                sqlParser.analyzeString(sys.argv[2])
                generate_intermediate_code(database_name)
            else:
                sqlParser.analyzeString(sys.argv[2])



if __name__ == '__main__':
    main = Main()
    argc = len(sys.argv)
    if argc > 2:
        if sys.argv[1] == '1':
            main.startAnalysis(0,sys.argv[3])
        else:
            print('python sql_analyzer.py filename | python sql_analyzer.py 1 \'string\' database_name')
    elif argc == 2:
        main.startAnalysis(1,'')
    else:
        print('python sql_analyzer.py filename | python sql_analyzer.py 1 \'string\'')