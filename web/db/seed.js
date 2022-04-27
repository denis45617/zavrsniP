const {
    Pool
} = require('pg');

const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'zavrsni',
    password: 'bazepodataka',
    port: 5432,
});



const sql_create_users = `CREATE TABLE users (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name text NOT NULL UNIQUE,
    password text NOT NULL
 )`;

const sql_create_users_id_index = `CREATE UNIQUE INDEX idx_usersId ON users(id)`;
const sql_create_users_name_index = `CREATE UNIQUE INDEX idx_usersName ON users(user_name)`;


const sql_create_sessions = `CREATE TABLE session (
    sid varchar NOT NULL COLLATE "default",
    sess json NOT NULL,
    expire timestamp(6) NOT NULL
  )
  WITH (OIDS=FALSE);`

const sql_create_session_index1 = `ALTER TABLE session ADD CONSTRAINT session_pkey PRIMARY KEY (sid) NOT DEFERRABLE INITIALLY IMMEDIATE`
const sql_create_session_index2 = `CREATE INDEX IDX_session_expire ON session(expire)`





const sql_insert_users = `INSERT INTO users (user_name,  password) VALUES ('admin', 'admin')`


let table_names = [
    "users",
    "sessions"
]

let tables = [
    sql_create_users,
    sql_create_sessions
];

let table_data = [
    sql_insert_users,
    undefined
]

let indexes = [
    sql_create_users_id_index,
    sql_create_session_index1,
    sql_create_session_index2
];

if ((tables.length !== table_data.length) || (tables.length !== table_names.length)) {
    console.log("tables, names and data arrays length mismatch.")
    return
}

//create tables and populate with data (if provided)

(async () => {
    for (let i = 0; i < tables.length; i++) {
        try {
            await pool.query(tables[i], [])
            if (table_data[i] !== undefined) {
                try {
                    await pool.query(table_data[i], [])
                } catch (err) {
                    return console.log(err.message); //pogreška pri dodavanju podataka u tablicu
                }
            }
        } catch (err) {
            console.log("Error creating table " + table_names[i])
            return console.log(err.message);
        }
    }

    //create indexes
    for (let i = 0; i < indexes.length; i++) {
        try {
            await pool.query(indexes[i], [])
        } catch (err) {
            //do nothing
        }
    }
})()
