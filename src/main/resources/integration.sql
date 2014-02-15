CREATE TABLE [JspActivity](
 [id] [bigint] PRIMARY KEY IDENTITY(1,1) NOT NULL, /*自增ID*/
 [apply_number][varchar](50) NOT NULL, /*申请单号*/
 [apply_date] [datetime] NOT NULL,  /*申请日期*/
 [apply_dept_no] [varchar](50) NOT NULL, /*申请部门编码*/
 [apply_person] [varchar](50) NOT NULL, /*申请人姓名和编码*/
 [approve_date] [datetime] NULL,  /*审核日期*/
 [approve_person] [varchar](50) NULL, /*审核人姓名和编码*/
 [item_name] [varchar](50) NOT NULL, /*器械包或一次性物品的名称*/
 [item_type] int NOT NULL, /*物品类型,1或2: 1 - 器械包; 2 - 一次性物品*/
 [item_no] [varchar](50) NOT NULL, /*器械包或一次性物品的基础数据编码*/
 [item_unit] [varchar](50) NOT NULL, /*器械包或一次性物品的计量单位*/
 [item_qty] [int] NOT NULL,   /*器械包或者一次性物品的申请数量*/
 [receiver_person] [varchar](50) NULL, /*接收人姓名和编码*/
 [bill_type] [varchar](50) NULL, /*申请单类型 （“科室领用”或“科室借用”,没区分则不用提供）*/
 [receive_type] [int] NULL, /*领用方式 ((0.下收下送、1.紧急请领)，如果没有区分就都是下收下送的，不用提供)*/
 [apply_type] [int] NULL, /*申请类型 (取值范围：1至4（1借用；2更换,即器械包的领用；3领用,即一次性物品的领用；4退库）)*/
 [update_time] [datetime] NOT NULL    /*修改日期*/
);

insert into EabaxOutLog 
(process_time, department_id, disposible_item_id, supplier_id, activity_id)
values ( getdate(), 0, 0, 0, 0);