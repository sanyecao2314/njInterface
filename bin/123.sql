

--¹ú¼Ò
select * from T_COS_Offer  for update 


INSERT INTO 
 wbia_jdbc_eventstore (event_id, object_key, 
 object_name, object_function, 
 event_priority, event_status, connector_ID) 
 VALUES(event_sequence.nextval, 'JVwsXAEdEADgAFiXrBAAfqpG0RI=', 
'NjtcuserT_Cos_Offer', 'Create', 
 1, 0, '001'); 
 
 
 select * from wbia_jdbc_eventstore for update 
 
 
 

 -- Create table
create table EVENTS
(
  EVENT_ID        NUMBER(20) not null,
  XID             VARCHAR2(200),
  OBJECT_KEY      VARCHAR2(80) not null,
  OBJECT_NAME     VARCHAR2(40) not null,
  OBJECT_FUNCTION VARCHAR2(40) not null,
  EVENT_PRIORITY  NUMBER(5) not null,
  EVENT_TIME      DATE default SYSDATE not null,
  EVENT_STATUS    NUMBER(5) not null,
  CONNECTOR_ID    VARCHAR2(40),
  EVENT_COMMENT   VARCHAR2(100)
)
-- Create/Recreate primary, unique and foreign key constraints 
alter table EVENTS
  add primary key (EVENT_ID)



 INSERT INTO 
 Object_name (event_id, object_key, 
 object_name, object_function, 
 event_priority, event_status, connector_ID) 
 VALUES(5, 'IyEucOP4ROyjWmbXJRzAt7ISq2U=', 
'T_COS_PTCR_DT', 'Create', 
 1, 1, '001');