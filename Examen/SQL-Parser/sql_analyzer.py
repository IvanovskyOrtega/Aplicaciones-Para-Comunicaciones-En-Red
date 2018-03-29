from ply.yacc import yacc
from ply.lex import lex
from sql_parser import parser
from sql_lexer import lexer
import sys

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

    def startAnalysis(self,option,argc):
        sqlParser = SQLParser()
        if option == 1:
            try:
                sqlParser.analyzeFile(str(sys.argv[1]))
            except:
                print('Try a valid file format')
        elif option == 0:
            sqlParser.analyzeString(sys.argv[2])


if __name__ == '__main__':
    main = Main()
    argc = len(sys.argv)
    if argc > 2:
        if sys.argv[1] == '1':
            main.startAnalysis(0,argc)
        else:
            print('python sql_analyzer.py filename | python sql_analyzer.py 1 \'string\'')
    elif argc == 2:
        main.startAnalysis(1,argc)
    else:
        print('python sql_analyzer.py filename | python sql_analyzer.py 1 \'string\'')