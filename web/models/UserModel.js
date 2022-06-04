const db = require('../db')
const bcrypt = require("bcrypt");


module.exports = class User {

    //konstruktor korisnika
    constructor(username, password) {
        this.user_name = username
        this.password = password
    }

    //dohvat korisnika na osnovu korisničkog imena
    static async fetchByUsername(username) {

        let results = await dbGetUserByName(username)
        let newUser = new User()

        if (results.length > 0) {
            newUser = new User(results[0].user_name, results[0].password)
            newUser.id = results[0].id
        }
        return newUser
    }


    //provjera da li game code pripada useru
    static async checkUserHasGameCode(user_name, game_code) {

        let results = await dbCheckUserHasGameCode(user_name, game_code);
        console.log(results.length)
        console.log(results.length > 0)
        return results.length > 0;

    }

    //provjera zaporke
    checkPassword(password) {
        return bcrypt.compareSync(password, this.password);
    }

    //pohrana korisnika u bazu podataka
    async persist() {
        try {
            await dbNewUser(this)
        } catch (err) {
            console.log("ERROR persisting user data: " + JSON.stringify(this))
        }
    }

}

//dohvat korisnika iz baze podataka na osnovu korisničkog imena (stupac user_name)
dbGetUserByName = async (user_name) => {
    const sql = `SELECT user_name, password
                 FROM users
                 WHERE user_name = '${user_name}'`;
    try {
        const result = await db.query(sql, []);
        return result.rows;
    } catch (err) {
        console.log(err);
        throw err
    }
};

//dohvat korisnika iz baze podataka na osnovu korisničkog imena (stupac user_name)
dbCheckUserHasGameCode = async (user_name, game_code) => {
    const sql = `SELECT *
                 FROM game_settings
                 WHERE user_name = '${user_name}'
                   AND game_code = '${game_code}'`;
    try {
        const result = await db.query(sql, []);
        return result.rows;
    } catch (err) {
        console.log(err);
        throw err
    }
};


//umetanje zapisa o korisniku u bazu podataka
dbNewUser = async (user) => {
    const sql = `INSERT INTO users (user_name, password)
                 VALUES ('${user.user_name}', '${user.password}')`;
    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}
