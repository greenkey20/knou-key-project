# 2023.8.12(토) 5h20 ec2 catalina.out ddl
alter table action_date
    drop
        foreign key FKgbpijdwf3gptxyy8cm0j3snh8;
alter table board
    drop
        foreign key FKsds8ox89wwf6aihinar49rmfy;
alter table board
    drop
        foreign key FKjqoq2j7d7bfkj5nwt40unuasy;
alter table plan
    drop
        foreign key FK8ib1c8odrbn5firtedh1wilgl;
alter table plan
    drop
        foreign key FK8sdagwoslgn44ysc0vybvti20;
alter table scrap
    drop
        foreign key FK2tu7ewv2kw9l5ka238j8ivsp0;
alter table scrap
    drop
        foreign key FKq0ff1jblgu8vrsh90u826qell;
alter table scrap
    drop
        foreign key FKr6p37sn60bsim5ok2nm7qjcjy;

drop table if exists action_date;
drop table if exists board;
drop table if exists category;
drop table if exists member;
drop table if exists plan;
drop table if exists scrap;

create table action_date (
                             is_done bit,
                             num_of_day integer,
                             num_of_month integer,
                             plan_action_quantity integer,
                             real_action_date date,
                             real_action_quantity integer,
                             review_score integer,
                             time_taken_for_real_action integer,
                             action_date_id bigint not null auto_increment,
                             created_at datetime(6),
                             last_modified_at datetime(6),
                             plan_id bigint,
                             date_format varchar(255),
                             date_type enum ('ACTION','DONE','GIVEUP','NORMALDAY','PAUSE','TODAY'),
                             memo varchar(255),
                             num_of_date varchar(255),
                             num_of_year varchar(255),
                             schedule varchar(255),
                             primary key (action_date_id)
) engine=InnoDB;

create table board (
                       read_count integer not null,
                       board_id bigint not null auto_increment,
                       created_at datetime(6),
                       last_modified_at datetime(6),
                       member_id bigint,
                       plan_id bigint,
                       board_type enum ('PLAN','SUCCESS') not null,
                       content varchar(255) not null,
                       title varchar(255) not null,
                       primary key (board_id)
) engine=InnoDB;

create table category (
                          category_id bigint not null auto_increment,
                          created_at datetime(6),
                          last_modified_at datetime(6),
                          name varchar(255) not null,
                          primary key (category_id)
) engine=InnoDB;

create table member (
                        age integer,
                        is_email_verified bit,
                        year_of_birth integer,
                        created_at datetime(6),
                        last_login_at datetime(6),
                        last_modified_at datetime(6),
                        member_id bigint not null auto_increment,
                        email varchar(255) not null,
                        gender enum ('FEMALE','MALE','OTHERS'),
                        member_platform enum ('GOOGLE','KAKAO','NAVER','WEBSITE') not null,
                        nickname varchar(255) not null,
                        password varchar(255),
                        profile_image_url varchar(255),
                        role enum ('ADMIN','USER') not null,
                        status enum ('ACTIVE','BANNED','QUIT') default 'ACTIVE' not null,
                        primary key (member_id)
) engine=InnoDB;

create table plan (
                      deadline_date date,
                      deadline_period_num integer,
                      frequency_factor float(53),
                      has_deadline bit not null,
                      has_start_date bit not null,
                      is_child bit,
                      is_measurable bit not null,
                      last_status_changed_at date,
                      quantity_per_day integer,
                      quantity_per_day_predicted integer,
                      start_date date,
                      total_duration_days integer,
                      total_num_of_actions integer,
                      total_quantity integer,
                      created_at datetime(6),
                      last_modified_at datetime(6),
                      member_id bigint,
                      parent_plan_id bigint,
                      plan_id bigint not null auto_increment,
                      chat_gpt_response LONGTEXT,
                      deadline_period varchar(255),
                      deadline_period_unit varchar(255),
                      deadline_type enum ('DATE','PERIOD'),
                      frequency_detail varchar(255) not null,
                      frequency_type enum ('DATE','EVERY','TIMES') not null,
                      object varchar(255) not null,
                      status enum ('ACTIVE','COMPLETE','DELETE','GIVEUP','PAUSE','RESULT') default 'ACTIVE' not null,
                      unit varchar(255),
                      primary key (plan_id)
) engine=InnoDB;

create table scrap (
                       category_id bigint,
                       created_at datetime(6),
                       last_modified_at datetime(6),
                       member_id bigint,
                       plan_id bigint,
                       scrap_id bigint not null auto_increment,
                       primary key (scrap_id)
) engine=InnoDB;

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname);

alter table action_date
    add constraint FKgbpijdwf3gptxyy8cm0j3snh8
        foreign key (plan_id)
            references plan (plan_id);

alter table board
    add constraint FKsds8ox89wwf6aihinar49rmfy
        foreign key (member_id)
            references member (member_id);

alter table board
    add constraint FKjqoq2j7d7bfkj5nwt40unuasy
        foreign key (plan_id)
            references plan (plan_id);

alter table plan
    add constraint FK8ib1c8odrbn5firtedh1wilgl
        foreign key (member_id)
            references member (member_id);

alter table plan
    add constraint FK8sdagwoslgn44ysc0vybvti20
        foreign key (parent_plan_id)
            references plan (plan_id);

alter table scrap
    add constraint FK2tu7ewv2kw9l5ka238j8ivsp0
        foreign key (category_id)
            references category (category_id);

alter table scrap
    add constraint FKq0ff1jblgu8vrsh90u826qell
        foreign key (member_id)
            references member (member_id);

alter table scrap
    add constraint FKr6p37sn60bsim5ok2nm7qjcjy
        foreign key (plan_id)
            references plan (plan_id)