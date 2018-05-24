
# parsetab.py
# This file is automatically generated. Do not edit.
# pylint: disable=W,C,R
_tabversion = '3.10'

_lr_method = 'LALR'

_lr_signature = 'INSTRUCTIONSASSING ASTERISK COMMA CP CREATE DATABASE DATABASES DATATYPE DELETE DROP FROM ID INSERT INTO OP SELECT SEMICOLON SET SHOW TABLE TABLES UPDATE USE VALUE VALUES WHEREINSTRUCTIONS : INSTRUCTION INSTRUCTIONS\n                     | INSTRUCTIONINSTRUCTION : PETITION SEMICOLONPETITION : CREATE_DATABASE\n                | CREATE_TABLE\n                | DROP_DATABASE\n                | DROP_TABLE\n                | USE_DATABASE\n                | SHOW_DATABASES\n                | SHOW_TABLES\n                | INSERT_INTO_TABLE\n                | UPDATE_TABLE_FIELD\n                | DELETE_FROM_TABLE\n                | SELECT_FROM_TABLECREATE_DATABASE : CREATE DATABASE IDCREATE_TABLE : CREATE TABLE ID OP TABLE_FIELDS CPTABLE_FIELDS : ID DT COMMA TABLE_FIELDS\n                    | ID DTDT : DATATYPE\n          | DATATYPE OP VALUE CPDROP_DATABASE : DROP DATABASE IDDROP_TABLE : DROP TABLE IDUSE_DATABASE : USE IDSHOW_DATABASES : SHOW DATABASESSHOW_TABLES : SHOW TABLESINSERT_INTO_TABLE : INSERT INTO ID VALUES OP VALUES_LIST CPVALUES_LIST : VALUE COMMA VALUES_LIST\n                   | VALUEUPDATE_TABLE_FIELD : UPDATE ID SET ID ASSING VALUEDELETE_FROM_TABLE : DELETE FROM ID WHERE ID ASSING VALUESELECT_FROM_TABLE : SELECT ASTERISK FROM ID '
    
_lr_action_items = {'CREATE':([0,2,24,],[15,15,-3,]),'DROP':([0,2,24,],[16,16,-3,]),'USE':([0,2,24,],[17,17,-3,]),'SHOW':([0,2,24,],[18,18,-3,]),'INSERT':([0,2,24,],[19,19,-3,]),'UPDATE':([0,2,24,],[20,20,-3,]),'DELETE':([0,2,24,],[21,21,-3,]),'SELECT':([0,2,24,],[22,22,-3,]),'$end':([1,2,23,24,],[0,-2,-1,-3,]),'SEMICOLON':([3,4,5,6,7,8,9,10,11,12,13,14,29,30,31,36,38,39,48,56,59,63,65,],[24,-4,-5,-6,-7,-8,-9,-10,-11,-12,-13,-14,-23,-24,-25,-15,-21,-22,-31,-16,-29,-26,-30,]),'DATABASE':([15,16,],[25,27,]),'TABLE':([15,16,],[26,28,]),'ID':([17,20,25,26,27,28,32,34,41,43,44,47,61,],[29,33,36,37,38,39,40,42,46,48,49,53,49,]),'DATABASES':([18,],[30,]),'TABLES':([18,],[31,]),'INTO':([19,],[32,]),'FROM':([21,35,],[34,43,]),'ASTERISK':([22,],[35,]),'SET':([33,],[41,]),'OP':([37,45,55,],[44,51,62,]),'VALUES':([40,],[45,]),'WHERE':([42,],[47,]),'ASSING':([46,53,],[52,60,]),'DATATYPE':([49,],[55,]),'CP':([50,54,55,57,58,66,67,68,69,],[56,-18,-19,63,-28,-17,69,-27,-20,]),'VALUE':([51,52,60,62,64,],[58,59,65,67,58,]),'COMMA':([54,55,58,69,],[61,-19,64,-20,]),}

_lr_action = {}
for _k, _v in _lr_action_items.items():
   for _x,_y in zip(_v[0],_v[1]):
      if not _x in _lr_action:  _lr_action[_x] = {}
      _lr_action[_x][_k] = _y
del _lr_action_items

_lr_goto_items = {'INSTRUCTIONS':([0,2,],[1,23,]),'INSTRUCTION':([0,2,],[2,2,]),'PETITION':([0,2,],[3,3,]),'CREATE_DATABASE':([0,2,],[4,4,]),'CREATE_TABLE':([0,2,],[5,5,]),'DROP_DATABASE':([0,2,],[6,6,]),'DROP_TABLE':([0,2,],[7,7,]),'USE_DATABASE':([0,2,],[8,8,]),'SHOW_DATABASES':([0,2,],[9,9,]),'SHOW_TABLES':([0,2,],[10,10,]),'INSERT_INTO_TABLE':([0,2,],[11,11,]),'UPDATE_TABLE_FIELD':([0,2,],[12,12,]),'DELETE_FROM_TABLE':([0,2,],[13,13,]),'SELECT_FROM_TABLE':([0,2,],[14,14,]),'TABLE_FIELDS':([44,61,],[50,66,]),'DT':([49,],[54,]),'VALUES_LIST':([51,64,],[57,68,]),}

_lr_goto = {}
for _k, _v in _lr_goto_items.items():
   for _x, _y in zip(_v[0], _v[1]):
       if not _x in _lr_goto: _lr_goto[_x] = {}
       _lr_goto[_x][_k] = _y
del _lr_goto_items
_lr_productions = [
  ("S' -> INSTRUCTIONS","S'",1,None,None,None),
  ('INSTRUCTIONS -> INSTRUCTION INSTRUCTIONS','INSTRUCTIONS',2,'p_INSTRUCTIONS','sql_parser.py',12),
  ('INSTRUCTIONS -> INSTRUCTION','INSTRUCTIONS',1,'p_INSTRUCTIONS','sql_parser.py',13),
  ('INSTRUCTION -> PETITION SEMICOLON','INSTRUCTION',2,'p_INSTRUCTION','sql_parser.py',16),
  ('PETITION -> CREATE_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',19),
  ('PETITION -> CREATE_TABLE','PETITION',1,'p_PETITION','sql_parser.py',20),
  ('PETITION -> DROP_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',21),
  ('PETITION -> DROP_TABLE','PETITION',1,'p_PETITION','sql_parser.py',22),
  ('PETITION -> USE_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',23),
  ('PETITION -> SHOW_DATABASES','PETITION',1,'p_PETITION','sql_parser.py',24),
  ('PETITION -> SHOW_TABLES','PETITION',1,'p_PETITION','sql_parser.py',25),
  ('PETITION -> INSERT_INTO_TABLE','PETITION',1,'p_PETITION','sql_parser.py',26),
  ('PETITION -> UPDATE_TABLE_FIELD','PETITION',1,'p_PETITION','sql_parser.py',27),
  ('PETITION -> DELETE_FROM_TABLE','PETITION',1,'p_PETITION','sql_parser.py',28),
  ('PETITION -> SELECT_FROM_TABLE','PETITION',1,'p_PETITION','sql_parser.py',29),
  ('CREATE_DATABASE -> CREATE DATABASE ID','CREATE_DATABASE',3,'p_CREATE_DATABASE','sql_parser.py',32),
  ('CREATE_TABLE -> CREATE TABLE ID OP TABLE_FIELDS CP','CREATE_TABLE',6,'p_CREATE_TABLE','sql_parser.py',36),
  ('TABLE_FIELDS -> ID DT COMMA TABLE_FIELDS','TABLE_FIELDS',4,'p_TABLE_FIELDS','sql_parser.py',42),
  ('TABLE_FIELDS -> ID DT','TABLE_FIELDS',2,'p_TABLE_FIELDS','sql_parser.py',43),
  ('DT -> DATATYPE','DT',1,'p_DT','sql_parser.py',47),
  ('DT -> DATATYPE OP VALUE CP','DT',4,'p_DT','sql_parser.py',48),
  ('DROP_DATABASE -> DROP DATABASE ID','DROP_DATABASE',3,'p_DROP_DATABASE','sql_parser.py',68),
  ('DROP_TABLE -> DROP TABLE ID','DROP_TABLE',3,'p_DROP_TABLE','sql_parser.py',72),
  ('USE_DATABASE -> USE ID','USE_DATABASE',2,'p_USE_DATABASE','sql_parser.py',76),
  ('SHOW_DATABASES -> SHOW DATABASES','SHOW_DATABASES',2,'p_SHOW_DATABASES','sql_parser.py',80),
  ('SHOW_TABLES -> SHOW TABLES','SHOW_TABLES',2,'p_SHOW_TABLES','sql_parser.py',84),
  ('INSERT_INTO_TABLE -> INSERT INTO ID VALUES OP VALUES_LIST CP','INSERT_INTO_TABLE',7,'p_INSERT_INTO_TABLE','sql_parser.py',88),
  ('VALUES_LIST -> VALUE COMMA VALUES_LIST','VALUES_LIST',3,'p_VALUES_LIST','sql_parser.py',92),
  ('VALUES_LIST -> VALUE','VALUES_LIST',1,'p_VALUES_LIST','sql_parser.py',93),
  ('UPDATE_TABLE_FIELD -> UPDATE ID SET ID ASSING VALUE','UPDATE_TABLE_FIELD',6,'p_UPDATE_TABLE_FIELD','sql_parser.py',96),
  ('DELETE_FROM_TABLE -> DELETE FROM ID WHERE ID ASSING VALUE','DELETE_FROM_TABLE',7,'p_DELETE_FROM_TABLE','sql_parser.py',100),
  ('SELECT_FROM_TABLE -> SELECT ASTERISK FROM ID','SELECT_FROM_TABLE',4,'p_SELECT_FROM_TABLE','sql_parser.py',104),
]
