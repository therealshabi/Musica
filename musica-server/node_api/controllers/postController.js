var helpers = require('../config/helperFunctions.js');
var UserModel = require('../models/UserModel.js');
var PostModel = require('../models/PostModel.js');


module.exports = function(server, async_query){

  // route to get the posts of a particular user from the database
  server.get("/user/post/:email_address",function(req,res,next){
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    PostModel.find({email_address: req.params.email_address,private_post:false }, function (err, posts) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(posts === null || posts.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      helpers.success(res,next,posts);
    });
  });

  // route to get the private posts of a particular user from the database
  server.get("/user/post/private/:email_address",function(req,res,next){
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    PostModel.find({email_address: req.params.email_address, private_post:true}, function (err, posts) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(posts === null || posts.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      else{
      helpers.success(res,next,posts);
      }
    });
  });

  // post route to post a post from a particular user
	server.post("/posts",function(req,res,next){
		var post_model = new PostModel();
		post_model.email_address=req.params.email_address;
		post_model.post_title=req.params.post_title;
		post_model.post_info=req.params.post_info;
		post_model.post_genre_tag=req.params.post_genre_tag;
    post_model.user_profile_pic = req.params.user_profile_pic;
    post_model.username = req.params.username;
    post_model.post_time_stamp = req.params.post_time_stamp;
		post_model.user_like=[];
		post_model.user_love=[];
    post_model.hits = 0;
		post_model.post_album_pic=req.params.post_album_pic;
    post_model.post_song_url = req.params.post_song_url;
    post_model.private_post = req.params.private_post;
		post_model.save(function(err) {
			if(err){
				helpers.failure(res,next,err,500);
			}
      UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
        if(err) {
          helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
        }
        if(user === null){
          helpers.failure(res,next,'The specified user '+ req.params.email_address+' cannot be found in the database',404);
        }
        user.posts.push(post_model._id);
        user.save(function(err) {
          if(err) {
            helpers.failure(res,next,err,500);
          }
          else {
            helpers.success(res,next,post_model);
          }
        });
      });
		});
	});

  // route to update the user liked post
  server.put("/post/like/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     post.user_like.push(req.params.user_liked_email_address);
     post.hits = post.hits+1;
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });

  // route to update the user loved post
  server.put("/post/love/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }

   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     post.user_love.push(req.params.user_loved_email_address);
     post.hits = post.hits+1;
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });


  // route to update the user liked post (unliking the post)
  server.put("/post/unlike/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     var index = post.user_like.indexOf(req.params.user_liked_email_address);
     post.user_like.splice(index,1);
     post.hits = post.hits+1;
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });


  // route to update the user loved post (unloving the post)
  server.put("/post/unlove/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     var index = post.user_love.indexOf(req.params.user_loved_email_address);
     post.user_love.splice(index,1);
     post.hits = post.hits+1;
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });


  // route to get the homeStream post of a particular user
  server.get("/user/homeStreamPost/:email_address",function(req,res,next){
    var result=[];
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }

    PostModel.find({email_address: req.params.email_address , private_post:false}, function (err, posts) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(posts === null || posts.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      else{
      for(var i=0;i<posts.length;i++)
      {
          result.push(posts[i]);
      }
    }

      UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
        if(err) {
          helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
        }
        if(user === null) {
          helpers.failure(res,next,'This user does not exist',404);
        }

        var f_users = user.following;

        var pushDoc = function(item, callback) {
                  if(item) {
                    PostModel.find({ email_address: item,private_post:false}, function(err, post) {

                      if(post != null) {
                        for(var i=0;i<post.length;i++)
                        {
                            result.push(post[i]);
                        }
                        //Return to function which called this function
                        callback();
                      }
                      else callback();
                    });
                  }
                };

  //This function will call callback for each user following list to pushDoc function
                async_query.forEach(f_users, pushDoc , function(err) {
                  //err will be generated when finished traversing the error
                  if(err)
                    console.log(err);
                    helpers.success(res,next,result);
                });

    });

});
});


  // route to get the username of the users who liked a particular post
  server.get("/user/post/like/:id",function(req,res,next){
    req.assert('id','Id is required').notEmpty();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    var result=[]

    PostModel.findOne({ _id: req.params.id }, function (err, post) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching post from the database',500);
      }
      if(post === null || post.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      var users=post.user_like;

      var pushDoc = function(item, callback) {
                if(item) {
                  UserModel.find({ email_address: item}, function(err, temp) {

                    if(temp != null) {
                      result.push(temp[0]);
                      //Return to function which called this function
                      callback();
                    }
                    else callback();
                  });
                }
              };

//This function will call callback for each user following list to pushDoc function
              async_query.forEach(users, pushDoc , function(err) {
                //err will be generated when finished traversing the error
                if(err)
                  console.log(err);
                  helpers.success(res,next,result);
              });

    });

    });


  // route to get the username of the users who loved a particular post
  server.get("/user/post/love/:id",function(req,res,next){
    req.assert('id','Id is required').notEmpty();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    var result=[]

    PostModel.findOne({ _id: req.params.id }, function (err, post) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching post from the database',500);
      }
      if(post === null || post.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      var users=post.user_love;

      var pushDoc = function(item, callback) {
                if(item) {
                  UserModel.find({ email_address: item}, function(err, temp) {

                    if(temp != null) {
                      result.push(temp[0]);
                      //Return to function which called this function
                      callback();
                    }
                    else callback();
                  });
                }
              };

//This function will call callback for each user following list to pushDoc function
              async_query.forEach(users, pushDoc , function(err) {
                //err will be generated when finished traversing the error
                if(err)
                  console.log(err);
                  helpers.success(res,next,result);
              });

    });

    });


  // route to get the email address of the users who liked a particular post
  server.get("/user/post/like/email/:id",function(req,res,next){
    req.assert('id','Id is required').notEmpty();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }

    PostModel.findOne({ _id: req.params.id }, function (err, post) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching post from the database',500);
      }
      if(post === null || post.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      else{
        helpers.success(res,next,post.user_like);
      }

      });
    });


  // route to get the email address of the users who loved a particular post
  server.get("/user/post/love/email/:id",function(req,res,next){
    req.assert('id','Id is required').notEmpty();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }

    PostModel.findOne({ _id: req.params.id }, function (err, post) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching post from the database',500);
      }
      if(post === null || post.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      else{
        helpers.success(res,next,post.user_love);
      }

      });
    });


    // route to update the hits of post
    server.put("/post/hits/:id",function(req,res,next){
     req.assert('id','Id is required').notEmpty();

     var errors = req.validationErrors();
     if (errors) {
       helpers.failure(res,next,errors,404);
     }

     PostModel.findOne({ _id: req.params.id }, function (err, post) {
       if(err) {
         helpers.failure(res,next,'Something went wrong while fetching from the database',500);
       }
       if(post === null){
         helpers.failure(res,next,'The specified user cannot be found in the database',404);
       }
       post.hits = post.hits+1;
       post.save(function(err) {
         if(err) {
           helpers.failure(res,next,'Smething went wrong!',500);
         }
         else {
           helpers.success(res,next,post);
         }
       });
     });
    });



}
