var helpers = require('../config/helperFunctions.js');
var UserModel = require('../models/UserModel.js');
var PostModel = require('../models/PostModel.js');


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
		user.user_info="";
		user.save(function(err) {
			if(err){
			helpers.failure(res,next,err,500);
			}
			helpers.success(res,next,user);
		});
	});

//Route for Searching User based on substring or prefix for Search Activity
	server.get('/user/search/:name_prefix',function(req, res, next){
		var name = req.params.name_prefix;
		UserModel.find({user_name: new RegExp('^'+name, "i")}, function(err, users) {
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
			}
			if(users === null || users.length===0){
				helpers.failure(res,next,'No User found',404);
			}
			helpers.success(res,next,users);
		});
	});

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

	// route to update the user following list (unfollow the user)
	server.put("/unfollowing/:email_address",function(req,res,next){
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
			var index = user.following.indexOf(req.params.follower_email_address);
			user.following.splice(index,1);
			user.save(function(err) {
				if(err) {
					helpers.failure(res,next,err,500);
				}
				else {
					//If followed successfully then also update the follower list of the user which you've followed
					UserModel.findOne({email_address: req.params.follower_email_address }, function (err, followingUser) {
						if(err) {
							helpers.failure(res,next,'Something went wrong while user fetching from the database',500);
						}
						if(followingUser === null){
							helpers.failure(res,next,'The specified user '+ req.params.follower_email_address +' cannot be found in the database',404);

						}
						else{
						var index = followingUser.followers.indexOf(req.params.email_address);
						followingUser.followers.splice(index,1);
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

	server.post("/user/description",function(req,res,next){
			var email = req.params.email_address;
			var description = req.params.description;
			UserModel.findOne({email_address:email}, function(err, user){
				if(err) {
					helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
				}
				if(user === null){
					helpers.failure(res,next,'The specified user cannot be found in the database',404);
				}
				user.user_info = description;
				user.save(function(err) {
					if(err) {
						helpers.failure(res,next,err,500);
					}
					else {
						helpers.success(res,next,user);
					}
				});
			});
	});

	server.get("/user/description/:email_address",function(req,res,next){
		var email = req.params.email_address;
		UserModel.findOne({email_address:email}, function(err, user){
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
			}
			if(user === null){
				helpers.failure(res,next,'The specified user cannot be found in the database',404);
			}
			var description = user.user_info;
					helpers.success(res,next,description);
		});
	});

	// route to get the user following users
	server.get("/user/find/following/:email_address",function(req,res,next){
		var email = req.params.email_address;
		UserModel.findOne({email_address:email}, function(err, user){
			if(err) {
				helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
			}
			if(user === null){
				helpers.failure(res,next,'The specified user cannot be found in the database',404);
			}
			var users = user.following;
					helpers.success(res,next,users);
		});
	});


}
