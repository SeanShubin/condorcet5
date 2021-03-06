select user.name     as user,
       election.name as election,
       confirmation,
       when_cast
from ballot
         inner join user
                    on ballot.user_id = user.id
         inner join election
                    on ballot.election_id = election.id
where election_id = (select id from election where name = ?)
