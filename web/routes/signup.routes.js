const express = require('express');
const router = express.Router();
const User = require('../models/UserModel')



//vrati signup stranicu
router.get('/', function (req, res, next) {
    res.render('signup', {
        title: 'Register a new user',
        linkActive: 'signup',
        user: req.session.user,
        err: undefined
    });
});


router.post('/', function (req, res, next) {
    (async () => {
        //provjeri istovjetnost unesenenih zaporki
        if (req.body.password1 !== req.body.password2) {
            res.render('Signup', {
                title: 'Register a new user',
                linkActive: 'signup',
                user: req.session.user,
                err: "Password entries do not match"
            });
            return;
        }

        //dobavi podatke o korisniku iz baze podataka
        let user = await User.fetchByUsername(req.body.username);


        //ako korisnik postoji, javi grešku
        if (user.user_name !== undefined) {
            res.render('Signup', {
                title: 'Register a new user',
                linkActive: 'signup',
                user: req.session.user,
                err: "Username already exists"
            });
            return;
        }

        //registriraj novog korisnika
        user = new User(req.body.username,  req.body.password1);
        await user.persist();

        req.session.user = user;
        res.redirect('/');
    })()

});


module.exports = router;
