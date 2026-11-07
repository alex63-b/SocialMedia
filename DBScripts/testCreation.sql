CREATE TABLE IF NOT EXISTS testUsers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
    );

-- o prietenie (id1, id2) se salveaza o singura data, cu id1 < id2
CREATE TABLE IF NOT EXISTS testFriendships (
    id1 BIGINT REFERENCES testUsers (id) ON DELETE CASCADE,
    id2 BIGINT REFERENCES testUsers (id) ON DELETE CASCADE,
    friendshipDate TIMESTAMP,
    PRIMARY KEY (id1, id2),
    CHECK (id1 < id2)
    );