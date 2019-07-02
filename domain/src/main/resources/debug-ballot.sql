select ballot.id,
       user.name     user,
       election.name election,
       ballot.confirmation,
       ballot.when_cast,
       ballot.user_id,
       ballot.election_id
from ballot
         inner join user on ballot.user_id = user.id
         inner join election on ballot.election_id = election.id