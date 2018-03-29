
# parsetab.py
# This file is automatically generated. Do not edit.
# pylint: disable=W,C,R
_tabversion = '3.10'

_lr_method = 'LALR'

_lr_signature = 'INSTRUCTIONSASSING ASTERISK COMMA CP CREATE DATABASE DATABASES DATATYPE DELETE DROP FROM ID INSERT INTO OP SELECT SEMICOLON SET SHOW TABLE TABLES UPDATE USE VALUE VALUES WHEREINSTRUCTIONS : INSTRUCTION INSTRUCTIONS\n                     | INSTRUCTIONINSTRUCTION : PETITION SEMICOLONPETITION : CREATE_DATABASE\n                | CREATE_TABLE\n                | DROP_DATABASE\n                | DROP_TABLE\n                | USE_DATABASE\n                | SHOW_DATABASES\n                | SHOW_TABLES\n                | INSERT_INTO_TABLE\n                | UPDATE_TABLE_FIELD\n                | DELETE_FROM_TABLE\n                | SELECT_FROM_TABLECREATE_DATABASE : CREATE DATABASE IDCREATE_TABLE : CREATE TABLE ID OP TABLE_FIELDS CPTABLE_FIELDS : ID DT COMMA TABLE_FIELDS\n                    | ID DTDT : DATATYPE\n          | DATATYPE OP VALUE CPDROP_DATABASE : DROP DATABASE IDDROP_TABLE : DROP TABLE IDUSE_DATABASE : USE IDSHOW_DATABASES : SHOW DATABASESSHOW_TABLES : SHOW TABLESINSERT_INTO_TABLE : INSERT INTO ID VALUES OP VALUES_LIST CPVALUES_LIST : VALUE COMMA VALUES_LIST\n                   | VALUEUPDATE_TABLE_FIELD : UPDATE ID SET ID ASSING VALUEDELETE_FROM_TABLE : DELETE FROM ID WHERE ID ASSING VALUESELECT_FROM_TABLE : SELECT ASTERISK FROM ID '
    
_lr_action_items = {'USE':([0,15,35,],[1,1,-3,]),'ASSING':([47,53,],[52,60,]),'SELECT':([0,15,35,],[10,10,-3,]),'INSERT':([0,15,35,],[6,6,-3,]),'SET':([33,],[42,]),'SEMICOLON':([2,3,4,9,11,12,13,17,18,19,20,22,23,30,31,37,39,40,46,56,59,62,65,],[-5,-13,-10,-12,-7,-11,-4,-9,-14,-6,-8,35,-23,-25,-24,-15,-22,-21,-31,-16,-29,-26,-30,]),'DATATYPE':([51,],[57,]),'CREATE':([0,15,35,],[7,7,-3,]),'DROP':([0,15,35,],[8,8,-3,]),'COMMA':([54,57,58,69,],[61,-19,64,-20,]),'TABLE':([7,8,],[26,27,]),'$end':([5,15,32,35,],[0,-2,-1,-3,]),'SHOW':([0,15,35,],[14,14,-3,]),'INTO':([6,],[24,]),'ASTERISK':([10,],[29,]),'UPDATE':([0,15,35,],[16,16,-3,]),'VALUE':([49,52,60,61,63,],[54,59,65,54,67,]),'DATABASES':([14,],[31,]),'CP':([50,54,55,57,58,66,67,68,69,],[56,-28,62,-19,-18,-27,69,-17,-20,]),'WHERE':([43,],[48,]),'ID':([1,16,24,25,26,27,28,34,41,42,45,48,64,],[23,33,36,37,38,39,40,43,46,47,51,53,51,]),'TABLES':([14,],[30,]),'FROM':([21,29,],[34,41,]),'DATABASE':([7,8,],[25,28,]),'VALUES':([36,],[44,]),'DELETE':([0,15,35,],[21,21,-3,]),'OP':([38,44,57,],[45,49,63,]),}

_lr_action = {}
for _k, _v in _lr_action_items.items():
   for _x,_y in zip(_v[0],_v[1]):
      if not _x in _lr_action:  _lr_action[_x] = {}
      _lr_action[_x][_k] = _y
del _lr_action_items

_lr_goto_items = {'CREATE_DATABASE':([0,15,],[13,13,]),'DROP_DATABASE':([0,15,],[19,19,]),'INSTRUCTION':([0,15,],[15,15,]),'UPDATE_TABLE_FIELD':([0,15,],[9,9,]),'SHOW_TABLES':([0,15,],[4,4,]),'USE_DATABASE':([0,15,],[20,20,]),'SHOW_DATABASES':([0,15,],[17,17,]),'DROP_TABLE':([0,15,],[11,11,]),'CREATE_TABLE':([0,15,],[2,2,]),'SELECT_FROM_TABLE':([0,15,],[18,18,]),'DELETE_FROM_TABLE':([0,15,],[3,3,]),'TABLE_FIELDS':([45,64,],[50,68,]),'VALUES_LIST':([49,61,],[55,66,]),'INSERT_INTO_TABLE':([0,15,],[12,12,]),'DT':([51,],[58,]),'PETITION':([0,15,],[22,22,]),'INSTRUCTIONS':([0,15,],[5,32,]),}

_lr_goto = {}
for _k, _v in _lr_goto_items.items():
   for _x, _y in zip(_v[0], _v[1]):
       if not _x in _lr_goto: _lr_goto[_x] = {}
       _lr_goto[_x][_k] = _y
del _lr_goto_items
_lr_productions = [
  ("S' -> INSTRUCTIONS","S'",1,None,None,None),
  ('INSTRUCTIONS -> INSTRUCTION INSTRUCTIONS','INSTRUCTIONS',2,'p_INSTRUCTIONS','sql_parser.py',11),
  ('INSTRUCTIONS -> INSTRUCTION','INSTRUCTIONS',1,'p_INSTRUCTIONS','sql_parser.py',12),
  ('INSTRUCTION -> PETITION SEMICOLON','INSTRUCTION',2,'p_INSTRUCTION','sql_parser.py',15),
  ('PETITION -> CREATE_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',18),
  ('PETITION -> CREATE_TABLE','PETITION',1,'p_PETITION','sql_parser.py',19),
  ('PETITION -> DROP_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',20),
  ('PETITION -> DROP_TABLE','PETITION',1,'p_PETITION','sql_parser.py',21),
  ('PETITION -> USE_DATABASE','PETITION',1,'p_PETITION','sql_parser.py',22),
  ('PETITION -> SHOW_DATABASES','PETITION',1,'p_PETITION','sql_parser.py',23),
  ('PETITION -> SHOW_TABLES','PETITION',1,'p_PETITION','sql_parser.py',24),
  ('PETITION -> INSERT_INTO_TABLE','PETITION',1,'p_PETITION','sql_parser.py',25),
  ('PETITION -> UPDATE_TABLE_FIELD','PETITION',1,'p_PETITION','sql_parser.py',26),
  ('PETITION -> DELETE_FROM_TABLE','PETITION',1,'p_PETITION','sql_parser.py',27),
  ('PETITION -> SELECT_FROM_TABLE','PETITION',1,'p_PETITION','sql_parser.py',28),
  ('CREATE_DATABASE -> CREATE DATABASE ID','CREATE_DATABASE',3,'p_CREATE_DATABASE','sql_parser.py',31),
  ('CREATE_TABLE -> CREATE TABLE ID OP TABLE_FIELDS CP','CREATE_TABLE',6,'p_CREATE_TABLE','sql_parser.py',35),
  ('TABLE_FIELDS -> ID DT COMMA TABLE_FIELDS','TABLE_FIELDS',4,'p_TABLE_FIELDS','sql_parser.py',42),
  ('TABLE_FIELDS -> ID DT','TABLE_FIELDS',2,'p_TABLE_FIELDS','sql_parser.py',43),
  ('DT -> DATATYPE','DT',1,'p_DT','sql_parser.py',47),
  ('DT -> DATATYPE OP VALUE CP','DT',4,'p_DT','sql_parser.py',48),
  ('DROP_DATABASE -> DROP DATABASE ID','DROP_DATABASE',3,'p_DROP_DATABASE','sql_parser.py',55),
  ('DROP_TABLE -> DROP TABLE ID','DROP_TABLE',3,'p_DROP_TABLE','sql_parser.py',59),
  ('USE_DATABASE -> USE ID','USE_DATABASE',2,'p_USE_DATABASE','sql_parser.py',63),
  ('SHOW_DATABASES -> SHOW DATABASES','SHOW_DATABASES',2,'p_SHOW_DATABASES','sql_parser.py',67),
  ('SHOW_TABLES -> SHOW TABLES','SHOW_TABLES',2,'p_SHOW_TABLES','sql_parser.py',71),
  ('INSERT_INTO_TABLE -> INSERT INTO ID VALUES OP VALUES_LIST CP','INSERT_INTO_TABLE',7,'p_INSERT_INTO_TABLE','sql_parser.py',75),
  ('VALUES_LIST -> VALUE COMMA VALUES_LIST','VALUES_LIST',3,'p_VALUES_LIST','sql_parser.py',79),
  ('VALUES_LIST -> VALUE','VALUES_LIST',1,'p_VALUES_LIST','sql_parser.py',80),
  ('UPDATE_TABLE_FIELD -> UPDATE ID SET ID ASSING VALUE','UPDATE_TABLE_FIELD',6,'p_UPDATE_TABLE_FIELD','sql_parser.py',83),
  ('DELETE_FROM_TABLE -> DELETE FROM ID WHERE ID ASSING VALUE','DELETE_FROM_TABLE',7,'p_DELETE_FROM_TABLE','sql_parser.py',87),
  ('SELECT_FROM_TABLE -> SELECT ASTERISK FROM ID','SELECT_FROM_TABLE',4,'p_SELECT_FROM_TABLE','sql_parser.py',91),
]