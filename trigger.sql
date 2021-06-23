
/* Issue no :181113-083D : Ingredient Communication - Website View Requirements*/
/* Created by:K. RAVALIKA SAI(MMM\A8MS8ZZ) */
/* Created on: Nov 19th , 2018*/

PRINT	''
PRINT	'BEGIN	TRANSACTION'
PRINT	''

BEGIN	TRANSACTION

PRINT	''
PRINT	'This script ran against the ' + DB_NAME() + ' database on the ' + @@Servername + ' server'
PRINT   'This script was run by ' + SUSER_NAME() + ' at ' + CONVERT(VARCHAR,getdate(),113) + ' on machine : ' + host_name()
PRINT	''
 
PRINT	'======================================='
PRINT	'****  BEGIN ISSUE 181113-083D  ****'
PRINT	'======================================='

--------------------------------------------------------------------------------------------------------------------


--trigger
CREATE TRIGGER MMM_ICS_DOC_FINAL
ON GENERATED_DOCUMENT  

 AFTER   
 UPDATE 
  AS BEGIN
  SET NOCOUNT ON;

  -- IF UPDATE (DOC_STATUS_CODE) 
  if exists (  select * from INSERTED where DOC_STATUS_CODE = 'final')
  begin

  insert into ONECDMS.. MMM_ICS_PRODUCTS select tn.DOC_GROUP_NUM,tn.TRADE_NAME  from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 


  declare @upc varchar(max)

   SELECT @upc=Stuff(
  (SELECT N', ' + A.UPC FROM 
  (select DISTINCT TN.DOC_GROUP_NUM, MMM_NY_ICS_UPC.UPC as UPC
from TRADE_NAME TN 
JOIN inserted ins
ON TN.TRADE_NAME_ID = ins.TRADE_NAME_ID
JOIN MMM_NY_ICS_UPC 
ON TN.TRADE_NAME_ID = MMM_NY_ICS_UPC.TRADE_NAME_ID
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 AND  m.status_code = 'FINAL')  as A
  
  FOR XML PATH(''),TYPE)
  .value('text()[1]','nvarchar(max)'),1,2,N'') 


  --insert into ONECDMS.. MMM_ICS_CODE select tn.DOC_GROUP_NUM,''  from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 

  declare @BRICK varchar(max)

  Select @BRICK=(select DISTINCT TN.DOC_GROUP_NUM, MMM_NY_ICS_BRICK.SCORE_CODE, MMM_NY_ICS_BRICK.SCORE_DESCRIPTION
from TRADE_NAME TN 
JOIN inserted ins
ON TN.TRADE_NAME_ID = ins.TRADE_NAME_ID
JOIN MMM_NY_ICS_BRICK
ON TN.MATERIAL_ID = MMM_NY_ICS_BRICK.MATERIAL_iD
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 AND  m.status_code = 'FINAL')

declare @EXT_DISC varchar(max)

select @EXT_DISC=(select DISTINCT TN.DOC_GROUP_NUM, MMM_NY_ICS_EXT_DISC.SCORE_CODE
from TRADE_NAME TN 
JOIN inserted ins
ON TN.TRADE_NAME_ID = ins.TRADE_NAME_ID
JOIN MMM_NY_ICS_EXT_DISC
ON TN.MATERIAL_ID = MMM_NY_ICS_EXT_DISC.MATERIAL_iD
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL')

declare @EXT_DISC_TWO varchar(max)

SELECT @EXT_DISC_TWO=(select DISTINCT TN.DOC_GROUP_NUM, MMM_NY_ICS_EXT_DISC_TWO.SCORE_CODE
from TRADE_NAME TN 
JOIN inserted ins
ON TN.TRADE_NAME_ID = ins.TRADE_NAME_ID
JOIN MMM_NY_ICS_EXT_DISC_TWO
ON TN.MATERIAL_ID = MMM_NY_ICS_EXT_DISC_TWO.MATERIAL_iD
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'
)

declare @USE_FORM varchar(max)

SELECT @USE_FORM= (select DISTINCT TN.DOC_GROUP_NUM, MMM_NY_ICS_USE_FORM.SCORE_DESCRIPTION
from TRADE_NAME TN 
JOIN inserted ins
ON TN.TRADE_NAME_ID = ins.TRADE_NAME_ID
JOIN MMM_NY_ICS_USE_FORM
ON TN.MATERIAL_ID = MMM_NY_ICS_USE_FORM.MATERIAL_iD
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 AND M.STATUS_CODE = 'FINAL'
)

insert into ONECDMS..MMM_ICS_SCORE_CODES select   tn.DOC_GROUP_NUM , @upc,@BRICK,@EXT_DISC,@EXT_DISC_TWO,@USE_FORM from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 


--

select distinct tn.doc_group_num
from trade_name tn
join inserted ins
on tn.trade_name_id = ins.trade_name_id
join MMM_NY_GHS_AQ
on MMM_NY_GHS_AQ.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'

If @@RowCount >0

insert into ONECDMS..MMM_ICS_GHS select tn.DOC_GROUP_NUM, 'GHS_AQ' from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 

select distinct tn.doc_group_num
from trade_name tn
join inserted ins
on tn.trade_name_id = INS.trade_name_id
join MMM_NY_GHS_MUTA
on MMM_NY_GHS_MUTA.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE INS.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'

If @@RowCount >0

insert into ONECDMS..MMM_ICS_GHS select tn.DOC_GROUP_NUM, 'GHS_MUTA' from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 


select distinct tn.doc_group_num
from trade_name tn
join inserted ins
on tn.trade_name_id = ins.trade_name_id
join MMM_NY_GHS_SENS
on MMM_NY_GHS_SENS.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'


If @@RowCount >0

insert into ONECDMS..MMM_ICS_GHS select tn.DOC_GROUP_NUM, 'GHS_SENS' from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 

select distinct tn.doc_group_num
from trade_name tn
join inserted ins
on tn.trade_name_id = ins.trade_name_id
join MMM_NY_GHS_EYE
on MMM_NY_GHS_EYE.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'

If @@RowCount >0

insert into ONECDMS..MMM_ICS_GHS select tn.DOC_GROUP_NUM, 'GHS_EYE' from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 

select distinct tn.doc_group_num
from trade_name tn
join inserted ins
on tn.trade_name_id = ins.trade_name_id
join MMM_NY_GHS_SKIN
on MMM_NY_GHS_SKIN.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_ID = TN.MATERIAL_ID
WHERE ins.TEMPLATE_ID = 1168 and m.status_code = 'FINAL'

If @@RowCount >0

insert into ONECDMS..MMM_ICS_GHS select tn.DOC_GROUP_NUM, 'GHS_SKIN' from inserted ins join trade_name tn on ins.TRADE_NAME_ID=tn.TRADE_NAME_ID 

--

Insert into ONECDMS.. MMM_ICS_ING_DET  select DISTINCT
doc_group_num,

CASE
WHEN MMM_ING_COMM_TAB.RESPONSE_CODE = 'Y' THEN MMM_ING_COMM_TAB.COC
ELSE ''
END AS COC,

CASE
WHEN MMM_ING_COMM_TAB.COMP_DESCRIPTOR_CODE = 'TRADE_SEC' THEN ISNULL(MMM_ING_COMM_TAB.NANO,'') + MMM_ING_COMM_TAB.PRINT_NAME
ELSE ISNULL(MMM_ING_COMM_TAB.NANO,'') + MMM_ING_COMM_TAB.NAME
END AS INGREDIENT,

CASE
WHEN MMM_ING_COMM_TAB.COMP_DESCRIPTOR_CODE = 'TRADE_SEC' THEN 'Withheld as CBI'
ELSE MMM_ING_COMM_TAB.COMP_CAS_NO
END AS CAS_NUMBER,

MMM_ING_COMM_TAB.VALUE_DESC,
PRINT_SEQUENCE_NUM,
MAX_VALUE

from trade_Name tn
join inserted ins
on tn.trade_name_id = ins.trade_name_Id
join MMM_ING_COMM_TAB
on MMM_ING_COMM_TAB.material_id = tn.material_id
JOIN MATERIAL M
ON M.MATERIAL_iD = TN.MATERIAL_iD
WHERE ins.TEMPLATE_ID = 1168 AND M.STATUS_CODE = 'FINAL' 
order by doc_group_num, print_sequence_num asc, max_value desc

end





  END

  --------------------------------------------------------------------------------------------------------------------



PRINT	'======================================'
PRINT	'****   END ISSUE 181113-083D  ****'
PRINT	'======================================'

PRINT	''
PRINT	'This script ran against the ' + DB_NAME() + ' database on the ' + @@Servername + ' server'
PRINT   'This script was run by ' + SUSER_NAME() + ' at ' + CONVERT(VARCHAR,getdate(),113) + ' on machine : ' + host_name()
PRINT	''
 
--ROLLBACK TRANSACTION
--COMMIT TRANSACTION