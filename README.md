# java-filmorate
Template repository for Filmorate project.

![This is an image](/filmorate.png)

examples of SQLs

1.find user by id

select * 
from users 
where USER_ID = ?;

2.add friend

insert into friends 
(user_id, friend_id) 
values (?, ?);

3.get friends list

select u.*
from friends as f
left join users as u on f.friend_id=u.user_id
where f.user_id=?;

4.get common friends

select *
from 
(select u.*
from friends as f
left join users as u on f.friend_id=u.user_id
where f.user_id=?
union all
select u.*  from friends as f
left join users as u on f.friend_id=u.user_id
where f.user_id=?) as common
group by common.USER_EMAIL, common.USER_LOGIN, common.USER_NAME, common.USER_BIRTHDAY
having count (common.user_ID)>1;