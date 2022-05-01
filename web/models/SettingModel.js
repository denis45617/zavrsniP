const db = require('../db')


module.exports = class Task {

    //konstruktor korisnika
    constructor(setting, game_code) {
        this.setting_id = undefined
        this.setting = setting
        this.game_code = game_code
    }

    //dohvat korisnika na osnovu korisničkog imena
    static async getGameCodeSettings(game_code) {
        try {
            let rezultat = await dbGetGameCodeSettings(game_code);
            return rezultat.rows;
        } catch (err) {
            console.log("ERROR getting game settings : " + JSON.stringify(this));
            throw err;
        }
    }

    //dohvat korisnika na osnovu korisničkog imena formatirano za mobitel
    static async getGameCodeSettingsMobile(game_code) {
        try {
            let rezultat = await dbGetGameCodeSettingsMobile(game_code);
            rezultat = rezultat.rows;

            let formatiraniRezultat = "";
            for (let i = 0; i < rezultat.length; ++i) {
                console.log(rezultat[i].setting_text)
                formatiraniRezultat += rezultat[i].setting_text;
                formatiraniRezultat += "#DELIMITER#";
            }
            formatiraniRezultat.replaceAll("\n", "")
            return formatiraniRezultat;
        } catch (err) {
            console.log("ERROR getting game settings : " + JSON.stringify(this));
            throw err;
        }
    }


    //stvaranje nove igre
    static async createNewTask(setting_desc, setting, game_code) {
        try {
            console.log("Setting: " + setting + " game_code: " + game_code)
            await dbNewTask(setting_desc, setting, game_code);
        } catch (err) {
            console.log("ERROR getting tasks for given game code : " + JSON.stringify(this));
            throw err;
        }

    }

    //brisanje postojeće  igre
    static async removeTask(username, setting_id) {
        try {
            await dbRemoveTask(username, setting_id);
        } catch (err) {
            console.log("ERROR removing game code : " + JSON.stringify(this));
            throw err;
        }

    }

}

//dohvat gamecode iz baze podataka na osnovu korisničkog imena (stupac user_name)
dbGetGameCodeSettings = async (game_code) => {
    const sql = `SELECT *
                 FROM setting
                 WHERE game_code = '${game_code}'
                   and is_deleted = 0`;
    try {
        return await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
};

//dohvat gamecode iz baze podataka na osnovu korisničkog imena (stupac user_name) formatirano za mobitel
dbGetGameCodeSettingsMobile = async (game_code) => {
    const sql = `SELECT *
                 FROM setting
                 WHERE game_code = '${game_code}'
                   and is_deleted = 0`;
    try {
        return await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
};


//dodavenje novog gameCode u bazu podataka
dbNewTask = async (setting_desc, setting, game_code) => {
    const sql = `INSERT INTO setting (setting_desc, setting_text, is_deleted, game_code)
                 VALUES ('${setting_desc}', '${setting}', 0, '${game_code}')`;
    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}


//brisanje zapisa o gameCode u bazu podataka
dbRemoveTask = async (user_name, setting_id) => {
    const sql = `UPDATE setting
                 SET is_deleted = 1
                 from game_settings
                 where user_name = '${user_name}'
                   and setting.game_code = game_settings.game_code
                   and setting_id = '${setting_id}'`;
    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}
