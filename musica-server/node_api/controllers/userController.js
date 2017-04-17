var helpers = require('../config/helperFunctions.js');
var UserModel = require('../models/UserModel.js');
var PostModel = require('../models/PostModel.js');

//var k=0;

module.exports = function(server){

	// route to get all the users assigned in the database
	server.get("/",function(req,res,next){
		UserModel.find({}, function (err, users) {
			if(err){
				helpers.failure(res,next,err, 500);
			}
			helpers.success(res,next,users);
		});

	});

	// route to get a particular user based upon the username
	server.get("/users/:user_name",function(req,res,next){
		req.assert('user_name','Username is required').notEmpty();
		var errors = req.validationErrors();
		if (errors) {
			helpers.failure(res,next,errors[0],400);
		}
		UserModel.findOne({user_name: req.params.user_name }, function (err, user) {
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
			}
			if(user === null){
				helpers.failure(res,next,'The specified user cannot be found in the database',404);
			}
			helpers.success(res,next,user);
		});
	});

	// route to get a particular user based upon the email_address of the user
	server.get("/user/:email_address",function(req,res,next){
		req.assert('email_address','Email Address is required').notEmpty().isEmail();
		var errors = req.validationErrors();
		if (errors) {
			helpers.failure(res,next,errors[0],400);
		}
		UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
			}
			if(user === null){
				helpers.failure(res,next,'The specified user cannot be found in the database',404);
			}
			helpers.success(res,next,user);
		});
	});


	// route to enter the details of the user at the time of sign up
	server.post("/signup",function(req,res,next){
		req.assert('user_name','Username is required').notEmpty();
		req.assert('email_address','Email Address is required').notEmpty().isEmail();
		req.assert('password','Password is required').notEmpty();

		var errors = req.validationErrors();
		if (errors) {
			helpers.failure(res,next,errors,404);
		}
		var user = new UserModel();
		user.user_name=req.params.user_name;
		user.email_address=req.params.email_address;
		user.password=req.params.password;
		user.profile_pic = req.params.profile_pic_url;
		user.save(function(err) {
			if(err){
			helpers.failure(res,next,err,500);
			}
			helpers.success(res,next,user);
		});
	});

	// // route to update the user followers list
	// server.put("/followers/:email_address",function(req,res,next){
	// 	req.assert('email_address','Email Address is required').notEmpty().isEmail();
	//
	// 	var errors = req.validationErrors();
	// 	if (errors) {
	// 		helpers.failure(res,next,errors,404);
	// 	}
	// 	UserModel.findOne({ email_address: req.params.email_address }, function (err, user) {
	// 		if(err) {
	// 			helpers.failure(res,next,'Something went wrong while fetching from the database',500);
	// 		}
	// 		if(user === null){
	// 			helpers.failure(res,next,'The specified user cannot be found in the database',404);
	// 		}
	// 		user.followers.push(req.params.follower_email_address);
	// 		user.save(function(err) {
	// 			if(err) {
	// 				helpers.failure(res,next,'The user cannot be added into the database',500);
	// 			}
	// 			else {
	// 				helpers.success(res,next,user);
	// 			}
	// 		});
	// 		//helpers.success(res,next,user);
	// 	});
	// });

	// route to update the user following list
	server.put("/following/:email_address",function(req,res,next){
		req.assert('email_address','Email Address is required').notEmpty().isEmail();

		var errors = req.validationErrors();
		if (errors) {
			helpers.failure(res,next,errors,404);
		}

		UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching from the database',500);
			}
			if(user === null){
				helpers.failure(res,next,'The specified user ' + req.params.email_address +' cannot be found in the database',404);
			}
			user.following.push(req.params.follower_email_address);
			user.save(function(err) {
				if(err) {
					helpers.failure(res,next,err,500);
				}
				else {
					//If followed successfully then also update the follower list of the user which you've followed
					UserModel.findOne({email_address: req.params.follower_email_address }, function (err, followingUser) {
						if(err) {
							helpers.failure(res,next,'Something went wrong while user fetching from the database',500);
							var index = user.following.indexOf(req.params.follower_email_address);
							user.following.splice(index, 1);
							user.save(function(err) {
								if(err) {
									helpers.failure(res,next,err,500);
								}
							});
						}
						if(followingUser === null){
							helpers.failure(res,next,'The specified user '+ req.params.follower_email_address +' cannot be found in the database',404);
							var index = user.following.indexOf(req.params.follower_email_address);
							user.following.splice(index, 1);
							user.save(function(err) {
								if(err) {
									helpers.failure(res,next,err,500);
								}
							});
						}
						else{
						followingUser.followers.push(req.params.email_address);
						followingUser.save(function(err) {
							if(err) {
								helpers.failure(res,next,err,500);
							}
							else {
								helpers.success(res,next,user);
							}
						});
						}
					});
				}
			});
		});
	});
}
