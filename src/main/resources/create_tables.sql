/****** 对象:  Table [dbo].[Account]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Account](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[email] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[password] [varchar](128) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[role] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
 CONSTRAINT [PK_Account] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** 对象:  Table [dbo].[Department]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Department](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[number] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[name] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[mmDeptNo] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[update_time] [datetime] NOT NULL CONSTRAINT [DF_Department_update_time]  DEFAULT (getdate()),
 CONSTRAINT [PK_Department] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** 对象:  Table [dbo].[DisposableItem]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DisposableItem](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[number] [varchar](150) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[name] [varchar](150) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[alias] [varchar](50) COLLATE Chinese_PRC_CI_AS,
	[unit] [varchar](150) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[specification] [varchar](150) COLLATE Chinese_PRC_CI_AS NULL,
	[model] [varchar](150) COLLATE Chinese_PRC_CI_AS NULL,
	[supplier_name] [varchar](150) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[supplier_no] [varchar](150) COLLATE Chinese_PRC_CI_AS NULL,
	[producer_name] [varchar](150) COLLATE Chinese_PRC_CI_AS NULL,
	[registration_no] [varchar](150) COLLATE Chinese_PRC_CI_AS NULL,
	[purchase_price] [numeric](18, 3) NOT NULL,
	[sales_price] [numeric](18, 3) NOT NULL,
	[update_time] [datetime] NOT NULL CONSTRAINT [DF_DisposableItem_update_time]  DEFAULT (getdate()),
 CONSTRAINT [PK_DisposableItem] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[EabaxInLog]    Script Date: 06/25/2014 16:04:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[EabaxInLog](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [process_time] [datetime] NOT NULL,
  [instrm_set_id] [bigint] NOT NULL,
  [mm_activity_id] [bigint] NOT NULL,
  [eabax_revert_apply_id] [bigint] NOT NULL,
  [eabax_return_apply_id] [bigint] NOT NULL,
 CONSTRAINT [PK_EabaxInLog] PRIMARY KEY CLUSTERED 
(
  [id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** 对象:  Table [dbo].[EabaxOutLog]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[EabaxOutLog](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[process_time] [datetime] NOT NULL,
	[department_id] [bigint] NOT NULL,
	[disposible_item_id] [bigint] NOT NULL,
	[supplier_id] [bigint] NOT NULL,
	[activity_id] [bigint] NOT NULL,
	[revert_activity_id] [bigint] NOT NULL,
 CONSTRAINT [PK_EabaxOutLog] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** 对象:  Table [dbo].[InstrumentSet]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[InstrumentSet](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[number] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[name] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[unit] [varchar](30) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[price] [numeric](18, 3) NOT NULL,
	[is_valid] [int] NOT NULL,
	[update_time] [datetime] NOT NULL,
 CONSTRAINT [PK_InstrumentSet] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** 对象:  Table [dbo].[JspActivity]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[JspActivity](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[drawapply_id] [bigint] NOT NULL,
	[apply_detail_id] [bigint] NOT NULL,
	[apply_number] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[apply_date] [datetime] NOT NULL,
	[apply_dept_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[apply_person] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[approve_date] [datetime] NULL,
	[approve_person] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[item_name] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[item_type] [int] NOT NULL,
	[item_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[item_unit] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[item_qty] [int] NOT NULL,
	[receiver_person] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[bill_type] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[receive_type] [int] NULL,
	[apply_type] [int] NULL,
	[is_apply] [int] NOT NULL,
	[IsDelete] [int] NOT NULL,
	[update_time] [datetime] NOT NULL CONSTRAINT [DF_JspActivity_update_time]  DEFAULT (getdate()),
 CONSTRAINT [PK__JspActivity__1ED998B2] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** 对象:  Table [dbo].[MmActivity]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[MmActivity](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[data_type] [int] NOT NULL,
	[item_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[item_unit] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[item_price] [numeric](18, 3) NOT NULL,
	[item_qyt] [int] NULL,
	[item_amount] [numeric](18, 3) NULL,
	[produce_date] [datetime] NOT NULL,
	[due_date] [datetime] NOT NULL,
	[receive_dept_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[approve_person_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[fee_amount] [numeric](18, 3) NOT NULL,
	[billmaker_no] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[bill_date] [datetime] NOT NULL,
	[update_time] [datetime] NOT NULL,
 CONSTRAINT [PK__MmActivity__22AA2996] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** 对象:  Table [dbo].[Supplier]    脚本日期: 03/23/2014 10:30:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Supplier](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[number] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[name] [varchar](100) COLLATE Chinese_PRC_CI_AS NOT NULL,
	[contact] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[contact_phone] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[address] [varchar](300) COLLATE Chinese_PRC_CI_AS NULL,
	[enterprise_nature] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[legal_person] [varchar](50) COLLATE Chinese_PRC_CI_AS NULL,
	[update_time] [datetime] NOT NULL CONSTRAINT [DF_Supplier_update_time]  DEFAULT (getdate()),
 CONSTRAINT [PK_Supplier] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [IX_Supplier] UNIQUE NONCLUSTERED 
(
	[number] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF

/****** Object:  Table [dbo].[EabaxRevertLog]    Script Date: 06/24/2014 14:23:50 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[EabaxRevertLog](
  [id] [bigint] IDENTITY(1,1) NOT NULL,
  [drawapply_id] [bigint] NOT NULL,
  [is_handled] [int] NOT NULL,
  [update_time] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
  [id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[EabaxRevertLog] ADD  DEFAULT (getdate()) FOR [update_time]
GO
