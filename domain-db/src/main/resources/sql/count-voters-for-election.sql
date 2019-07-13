select count(id)
from voter
where election_id = (select id from election where name = ?)
