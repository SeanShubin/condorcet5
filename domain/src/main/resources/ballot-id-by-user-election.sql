select id
from ballot
where user_id = (select id from user where name = ?)
  and election_id = (select id from election where name = ?)
