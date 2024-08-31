use `xiaofan-api`;
-- 接口信息表
create table if not exists `interface_info`
(
    `id`             bigint                             not null auto_increment comment '主键' primary key,
    `name`           varchar(256)                       not null comment '接口名称',
    `description`    varchar(256)                       not null comment '接口描述',
    `url`            varchar(512)                       not null comment '接口地址',
    `requestHeader`  text                               not null comment '请求体',
    `responseHeader` text                               not null comment '响应头',
    `status`         int      default 0                 not null comment '接口状态 (0-关闭,1-开启)',
    `method`         varchar(256)                       not null comment '请求类型',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息表';

insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (0, '邓鹏涛', '秦立果', 'www.annabell-hills.net', '阎浩然', '赖博文', 0, '0tgCX', 9438454);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (20, '王浩宇', '冯烨华', 'www.jeraldine-ebert.org', '刘煜祺', '谭文博', 0, 'PwFaL', 2669583);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (211478140, '贺烨伟', '程浩然', 'www.lettie-schneider.org', '何峻熙', '贺泽洋', 0, 'uwXj', 38200);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (47653288, '蔡健柏', '严鸿涛', 'www.josefine-reinger.co', '钱旭尧', '董立果', 0, 'DuRjA', 4720);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (4193159126, '韩天磊', '莫明轩', 'www.aisha-koepp.info', '钟天磊', '罗鹏', 0, 'j5', 13990);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (11, '魏鹏涛', '邱昊焱', 'www.alfonzo-roberts.info', '陶健柏', '秦伟祺', 0, 'AN', 567049977);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (943234, '彭博超', '方果', 'www.scott-friesen.name', '邱熠彤', '郝智宸', 0, 'sN0', 8578868);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (32736708, '蔡修洁', '胡智辉', 'www.katharyn-quitzon.name', '万明轩', '任晟睿', 0, 'kO', 918);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (5912, '韦钰轩', '郭钰轩', 'www.frances-botsford.name', '郑弘文', '阎子默', 0, 'GVykv', 64370);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (2063154, '马烨华', '覃锦程', 'www.kacey-labadie.info', '贺熠彤', '秦天翊', 0, 'by', 601);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (4010, '韦子轩', '阎思聪', 'www.marybeth-rodriguez.name', '徐浩宇', '薛弘文', 0, 'E6L', 9203);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (3375945, '董航', '钱烨霖', 'www.cody-hyatt.net', '贺烨霖', '王锦程', 0, 'om1ga', 895);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (1051131193, '石明', '姜绍齐', 'www.ulrike-ebert.name', '刘哲瀚', '王修杰', 0, 'CE2', 2707);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (3585004, '薛浩然', '傅语堂', 'www.ferdinand-glover.co', '萧昊然', '贺子默', 0, 'JMv1', 889324058);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (90, '田浩轩', '薛苑博', 'www.carmen-okon.biz', '秦烨磊', '任嘉熙', 0, 'phN0x', 92016);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (516, '罗靖琪', '孙越彬', 'www.elvina-johnston.com', '叶烨磊', '汪嘉熙', 0, 'RQ3X', 25821);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (92107, '江烨霖', '莫煜祺', 'www.deidra-schumm.net', '龚耀杰', '于俊驰', 0, 'bwpu', 6208);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (16, '汪伟泽', '彭乐驹', 'www.troy-weissnat.co', '高修洁', '顾驰', 0, 'ujAL', 3493);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (151948, '雷鸿涛', '秦建辉', 'www.arthur-schumm.org', '蔡浩', '钱天磊', 0, '1z', 4332);
insert into `interface_info` (`id`, `name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`,
                              `userId`)
values (33557999, '赵思', '王笑愚', 'www.johnathan-romaguera.com', '谢嘉熙', '罗绍辉', 0, 'TFF', 888460);