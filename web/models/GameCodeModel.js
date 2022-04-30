const db = require('../db')

//razred User enkapsulira korisnika web trgovine
module.exports = class GameCode {

    //konstruktor korisnika
    constructor(game_code, user_name) {
        this.game_code = game_code
        this.user_name = user_name
    }

    //dohvat korisnika na osnovu korisničkog imena
    static async getUserGameCodes(username) {
        try {
            let rezultat = await dbGetCodesByUsername(username);
            return rezultat.rows;
        } catch (err) {
            console.log("ERROR getting GameCodes : " + JSON.stringify(this));
            throw err;
        }
    }




    //stvaranje nove igre
    static async createNewGameCode(username) {
        try {
            await dbNewGameCode(username);
        } catch (err) {
            console.log("ERROR getting GameCodes : " + JSON.stringify(this));
            throw err;
        }

    }

    //brisanje postojeće  igre
    static async removeGameCode(username, gameCode) {
        try {
            await dbRemoveGameCode(username, gameCode);
        } catch (err) {
            console.log("ERROR removing game code : " + JSON.stringify(this));
            throw err;
        }

    }

}

//dohvat gamecode iz baze podataka na osnovu korisničkog imena (stupac user_name)
dbGetCodesByUsername = async (user_name) => {
    const sql = `SELECT *
                 FROM game_settings
                 WHERE user_name = '${user_name}'
                   and is_deleted = 0`;
    try {
        return await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
};


//dodavenje novog gameCode u bazu podataka
dbNewGameCode = async (user_name) => {
    const sql = `INSERT INTO game_settings (user_name, is_deleted)
                 VALUES ('${user_name}', 0)`;
    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}


//brisanje zapisa o gameCode u bazu podataka
dbRemoveGameCode = async (user_name, gameCode) => {
    const sql = `UPDATE game_settings
                 SET is_deleted=1
                 where user_name = '${user_name}'
                   and game_code = '${gameCode}'`;
    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}
