from sql_lexer import lexer
import ply.yacc as yacc
import sql_lexer
import sys

tokens = sql_lexer.tokens
attributes = []
types = []
classnameL = []

def p_INSTRUCTIONS(p):
    '''INSTRUCTIONS : INSTRUCTION INSTRUCTIONS
                     | INSTRUCTION'''

def p_INSTRUCTION(p):
    '''INSTRUCTION : PETITION SEMICOLON'''

def p_PETITION(p):
    '''PETITION : CREATE_DATABASE
                | CREATE_TABLE
                | DROP_DATABASE
                | DROP_TABLE
                | USE_DATABASE
                | SHOW_DATABASES
                | SHOW_TABLES
                | INSERT_INTO_TABLE
                | UPDATE_TABLE_FIELD
                | DELETE_FROM_TABLE
                | SELECT_FROM_TABLE'''

def p_CREATE_DATABASE(p):
    '''CREATE_DATABASE : CREATE DATABASE ID'''
    print(0)

def p_CREATE_TABLE(p):
    '''CREATE_TABLE : CREATE TABLE ID OP TABLE_FIELDS CP'''
    classnameL.append(p[3])
    # generate_intermediate_code(classname,filename)
    print(3)

def p_TABLE_FIELDS(p):
    '''TABLE_FIELDS : ID DT COMMA TABLE_FIELDS
                    | ID DT'''
    attributes.append(p[1])

def p_DT(p):
    '''DT : DATATYPE
          | DATATYPE OP VALUE CP'''
    dt = p[1].lower()
    if(dt.lower() == 'byte'):
        types.append('Byte')
    elif(dt == 'short'):
        types.append('Short')
    elif(dt == 'int'):
        types.append('Integer')
    elif(dt == 'long'):
        types.append('Long')
    elif(dt == 'float'):
        types.append('Float')
    elif(dt == 'double'):
        types.append('Double')
    elif(dt == 'char'):
        types.append('String')
    elif(dt == 'boolean'):
        types.append('Boolean')

def p_DROP_DATABASE(p):
    '''DROP_DATABASE : DROP DATABASE ID'''
    print(1)

def p_DROP_TABLE(p):
    '''DROP_TABLE : DROP TABLE ID'''
    print(4)

def p_USE_DATABASE(p):
    '''USE_DATABASE : USE ID'''
    print(2)

def p_SHOW_DATABASES(p):
    '''SHOW_DATABASES : SHOW DATABASES'''
    print(6)

def p_SHOW_TABLES(p):
    '''SHOW_TABLES : SHOW TABLES'''
    print(7)

def p_INSERT_INTO_TABLE(p):
    '''INSERT_INTO_TABLE : INSERT INTO ID VALUES OP VALUES_LIST CP'''
    print(5)

def p_VALUES_LIST(p):
    '''VALUES_LIST : VALUE COMMA VALUES_LIST
                   | VALUE'''

def p_UPDATE_TABLE_FIELD(p):
    '''UPDATE_TABLE_FIELD : UPDATE ID SET ID ASSING VALUE'''
    print(8)

def p_DELETE_FROM_TABLE(p):
    '''DELETE_FROM_TABLE : DELETE FROM ID WHERE ID ASSING VALUE'''
    print(9)

def p_SELECT_FROM_TABLE(p):
    '''SELECT_FROM_TABLE : SELECT ASTERISK FROM ID '''
    print(10)


def p_error(p):
    if p:
        print("SYNTAX ERROR AT %s" % p.value)
        print(11)
    else:
        print("SYNTAX ERROR IN EOF")
        print(11)

parser = yacc.yacc(start='INSTRUCTIONS') 

if __name__ == '__main__':
    # You can test your string here
    if len(sys.argv) < 2:
        print('python sql_parser.py TestString')
        exit()
    else:
        parser.parse(sys.argv[1],lexer=lexer)