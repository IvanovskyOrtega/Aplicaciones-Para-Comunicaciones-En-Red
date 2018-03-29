import ply.lex as lex
import sys

# Lista de tokens
tokens = (
    'CREATE',
    'DROP',
    'USE',
    'INSERT',
    'SHOW',
    'UPDATE',
    'DELETE',
    'SELECT',
    'DATABASE',
    'TABLE',
    'DATABASES',
    'TABLES',
    'FROM',
    'INTO',
    'WHERE',
    'SET',
    'VALUES',
    'ASSING',
    'SEMICOLON',
    'COMMA',
    'ASTERISK',
    'DATATYPE',
    'VALUE',
    'ID',
    'OP',
    'CP',
)

reservedWords = (
    'create','CREATE','drop','DROP','use','USE',
    'insert','INSERT','show','SHOW','update',
    'UPDATE','delete','DELETE','select','SELECT',
    'database','DATABASE','table','TABLE','databases',
    'DATABASES','tables','TABLES','from','FROM',
    'into','INTO','where','WHERE','set','SET',
    'values','VALUES',
)

datatypes = (
    'byte','BYTE','short','SHORT',
    'int','INT','long','LONG','float','FLOAT','double',
    'DOUBLE','char','CHAR','boolean','BOOLEAN',
)

booleanValues = ('true','TRUE','false','FALSE')

# Definimos las expresiones regulares para los tokens
t_CREATE = r'(create)|(CREATE)'
t_DROP = r'(drop)|(DROP)'
t_USE = r'(use)|(USE)'
t_INSERT = r'(insert)|(INSERT)'
t_SHOW = r'(show)|(SHOW)'
t_UPDATE = r'(update)|(UPDATE)'
t_DELETE = r'(delete)|(DELETE)'
t_SELECT = r'(select)|(SELECT)'
t_SEMICOLON = r';'
t_COMMA = r','
t_ASTERISK = r'\*'
t_OP = r'\('
t_CP = r'\)'
t_DATABASE = r'(database)|(DATABASE)'
t_TABLE = r'(table)|(TABLE)'
t_DATABASES = r'(databases)|(DATABASES)'
t_TABLES = r'(tables)|(TABLES)'
t_FROM = r'(from)|(FROM)'
t_INTO = r'(into)|(INTO)'
t_WHERE = r'(where)|(WHERE)'
t_SET = r'(set)|(SET)'
t_VALUES = r'(values)|(VALUES)'
t_ASSING = r'='

# Ignoramos espacios, tabuladores y saltos de linea
t_ignore  = ' \t\n'

t_DATATYPE =(
    r'byte'
    r'BYTE'
    r'short'
    r'SHORT'
    r'int'
    r'INT'
    r'long'
    r'LONG'
    r'float'
    r'FLOAT'
    r'double'
    r'DOUBLE'
    r'char'
    r'CHAR'
    r'boolean'
    r'BOOLEAN'
)

def t_ID(t):
    r'[a-zA-Z_][a-zA-Z0-9_]+'
    if t.value not in reservedWords :
        if t.value not in datatypes:
            if t.value not in booleanValues:
                t.type = 'ID'
            else:
                t.type = 'VALUE'
        else:
            t.type = 'DATATYPE'
    else:
        t.type = t.value.upper()
    return t

def t_VALUE(t):
    r'([a-zA-Z0-9_\'\-.@ ]+)|([1-9][0-9]*.?[0-9]*)'
    if t.value not in reservedWords :
        if t.value not in datatypes:
            t.type = 'VALUE'
        else:
            t.type = 'DATATYPE'
    else:
        t.type = t.value.upper()
    return t



# Regla para el manejo de errores
def t_error(t):
    print("Caracter no reconocido %s" % t.value[0])
    t.lexer.skip(1)

# Construimos el lexer
lexer = lex.lex()

if __name__ == '__main__':
    # You can test your string here
    if len(sys.argv) < 2:
        print('python sql_lexer.py TestString')
        exit()
    else:
        lex.input(sys.argv[1])
        for tok in iter(lex.token, None):    
            print repr(tok.type), repr(tok.value)