create table if not exists pms_attr
(
    attr_id      bigint auto_increment comment '属性id'
        primary key,
    attr_name    char(30)     null comment '属性名',
    search_type  tinyint      null comment '是否需要检索[0-不需要，1-需要]',
    value_type   tinyint      null comment '值类型[0-为单个值，1-可以选择多个值]',
    icon         varchar(255) null comment '属性图标',
    value_select char(255)    null comment '可选值列表[用逗号分隔]',
    attr_type    tinyint      null comment '属性类型[0-销售属性，1-基本属性',
    enable       bigint       null comment '启用状态[0 - 禁用，1 - 启用]',
    catelog_id   bigint       null comment '所属分类',
    show_desc    tinyint      null comment '快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整'
)
    comment '商品属性' auto_increment = 17;

create table if not exists pms_attr_attrgroup_relation
(
    id            bigint auto_increment comment 'id'
        primary key,
    attr_id       bigint null comment '属性id',
    attr_group_id bigint null comment '属性分组id',
    attr_sort     int    null comment '属性组内排序'
)
    comment '属性&属性分组关联' auto_increment = 31;

create table if not exists pms_attr_group
(
    attr_group_id   bigint auto_increment comment '分组id'
        primary key,
    attr_group_name char(20)     null comment '组名',
    sort            int          null comment '排序',
    descript        varchar(255) null comment '描述',
    icon            varchar(255) null comment '组图标',
    catelog_id      bigint       null comment '所属分类id'
)
    comment '属性分组' auto_increment = 8;

create table if not exists pms_brand
(
    brand_id     bigint auto_increment comment '品牌id'
        primary key,
    name         char(50)      null comment '品牌名',
    logo         varchar(2000) null comment '品牌logo地址',
    descript     longtext      null comment '介绍',
    show_status  tinyint       null comment '显示状态[0-不显示；1-显示]',
    first_letter char          null comment '检索首字母',
    sort         int           null comment '排序'
)
    comment '品牌' auto_increment = 13;

create table if not exists pms_category
(
    cat_id        bigint auto_increment comment '分类id'
        primary key,
    name          char(50)  null comment '分类名称',
    parent_cid    bigint    null comment '父分类id',
    cat_level     int       null comment '层级',
    show_status   tinyint   null comment '是否显示[0-不显示，1显示]',
    sort          int       null comment '排序',
    icon          char(255) null comment '图标地址',
    product_unit  char(50)  null comment '计量单位',
    product_count int       null comment '商品数量'
)
    comment '商品三级分类' auto_increment = 1433;

create index parent_cid
    on pms_category (parent_cid);

create table if not exists pms_category_brand_relation
(
    id           bigint auto_increment
        primary key,
    brand_id     bigint       null comment '品牌id',
    catelog_id   bigint       null comment '分类id',
    brand_name   varchar(255) null,
    catelog_name varchar(255) null
)
    comment '品牌分类关联' auto_increment = 28;

create table if not exists pms_comment_replay
(
    id         bigint auto_increment comment 'id'
        primary key,
    comment_id bigint null comment '评论id',
    reply_id   bigint null comment '回复id'
)
    comment '商品评价回复关系';

create table if not exists pms_product_attr_value
(
    id         bigint auto_increment comment 'id'
        primary key,
    spu_id     bigint       null comment '商品id',
    attr_id    bigint       null comment '属性id',
    attr_name  varchar(200) null comment '属性名',
    attr_value varchar(200) null comment '属性值',
    attr_sort  int          null comment '顺序',
    quick_show tinyint      null comment '快速展示【是否展示在介绍上；0-否 1-是】'
)
    comment 'spu属性值' auto_increment = 68;

create table if not exists pms_sku_images
(
    id          bigint auto_increment comment 'id'
        primary key,
    sku_id      bigint       null comment 'sku_id',
    img_url     varchar(255) null comment '图片地址',
    img_sort    int          null comment '排序',
    default_img int          null comment '默认图[0 - 不是默认图，1 - 是默认图]'
)
    comment 'sku图片' auto_increment = 111;

create table if not exists pms_sku_info
(
    sku_id          bigint auto_increment comment 'skuId'
        primary key,
    spu_id          bigint         null comment 'spuId',
    sku_name        varchar(255)   null comment 'sku名称',
    sku_desc        varchar(2000)  null comment 'sku介绍描述',
    catalog_id      bigint         null comment '所属分类id',
    brand_id        bigint         null comment '品牌id',
    sku_default_img varchar(255)   null comment '默认图片',
    sku_title       varchar(255)   null comment '标题',
    sku_subtitle    varchar(2000)  null comment '副标题',
    price           decimal(18, 4) null comment '价格',
    sale_count      bigint         null comment '销量'
)
    comment 'sku信息' auto_increment = 27;

create table if not exists pms_sku_sale_attr_value
(
    id         bigint auto_increment comment 'id'
        primary key,
    sku_id     bigint       null comment 'sku_id',
    attr_id    bigint       null comment 'attr_id',
    attr_name  varchar(200) null comment '销售属性名',
    attr_value varchar(200) null comment '销售属性值',
    attr_sort  int          null comment '顺序'
)
    comment 'sku销售属性&值' auto_increment = 53;

create table if not exists pms_spu_comment
(
    id               bigint auto_increment comment 'id'
        primary key,
    sku_id           bigint        null comment 'sku_id',
    spu_id           bigint        null comment 'spu_id',
    spu_name         varchar(255)  null comment '商品名字',
    member_nick_name varchar(255)  null comment '会员昵称',
    star             tinyint(1)    null comment '星级',
    member_ip        varchar(64)   null comment '会员ip',
    create_time      datetime      null comment '创建时间',
    show_status      tinyint(1)    null comment '显示状态[0-不显示，1-显示]',
    spu_attributes   varchar(255)  null comment '购买时属性组合',
    likes_count      int           null comment '点赞数',
    reply_count      int           null comment '回复数',
    resources        varchar(1000) null comment '评论图片/视频[json数据；[{type:文件类型,url:资源路径}]]',
    content          text          null comment '内容',
    member_icon      varchar(255)  null comment '用户头像',
    comment_type     tinyint       null comment '评论类型[0 - 对商品的直接评论，1 - 对评论的回复]'
)
    comment '商品评价';

create table if not exists pms_spu_images
(
    id          bigint auto_increment comment 'id'
        primary key,
    spu_id      bigint       null comment 'spu_id',
    img_name    varchar(200) null comment '图片名',
    img_url     varchar(255) null comment '图片地址',
    img_sort    int          null comment '顺序',
    default_img tinyint      null comment '是否默认图'
)
    comment 'spu图片' auto_increment = 95;

create table if not exists pms_spu_info
(
    id              bigint auto_increment comment '商品id'
        primary key,
    spu_name        varchar(200)   null comment '商品名称',
    spu_description varchar(1000)  null comment '商品描述',
    catalog_id      bigint         null comment '所属分类id',
    brand_id        bigint         null comment '品牌id',
    weight          decimal(18, 4) null,
    publish_status  tinyint        null comment '上架状态[0 - 下架，1 - 上架]',
    create_time     datetime       null,
    update_time     datetime       null
)
    comment 'spu信息' auto_increment = 14;

create table if not exists pms_spu_info_desc
(
    spu_id  bigint   not null comment '商品id',
    decript longtext null comment '商品介绍',
    primary key (spu_id)
)
    comment 'spu信息介绍';

create table if not exists role_category_relation
(
    id          int auto_increment
        primary key,
    category_id int null,
    role_id     int null,
    constraint id
        unique (id)
);

create table if not exists undo_log
(
    id            bigint auto_increment
        primary key,
    branch_id     bigint       not null,
    xid           varchar(100) not null,
    context       varchar(128) not null,
    rollback_info longblob     not null,
    log_status    int          not null,
    log_created   datetime     not null,
    log_modified  datetime     not null,
    ext           varchar(100) null,
    constraint ux_undo_log
        unique (xid, branch_id)
)
    charset = utf8mb3;


