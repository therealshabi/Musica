var mongoose = require('mongoose');

var Schema = mongoose.Schema,
    ObjectId = Schema.ObjectId;

var UserSchema = new Schema({
    id    : ObjectId,
    user_name     : String,
    email_address     : String,
    password     : String,
    user_info     : String,
    profile_pic     : String,
    cover_pic     : String,
    posts : [String],
    followers     : [String],
    following     : [String]
});

// users here is the collection(table) name that we are
// going to create in mlab database
var UserModel = mongoose.model('users', UserSchema);

module.exports = UserModel;
