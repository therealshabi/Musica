var mongoose = require('mongoose');

var Schema = mongoose.Schema,
    ObjectId = Schema.ObjectId;

var PostSchema = new Schema({
    id    : ObjectId,
    email_address     : String,
    user_profile_pic : String,
    username : String,
    post_title     : String,
    post_info     : String,
    post_genre_tag     : String,
    user_like     : [String],
    user_love     : [String],
    private_post : Boolean,
    post_album_pic     : String,
    post_song_url : String,
    post_time_stamp: String,
    hits : Number
});

// users here is the collection(table) name that we are
// going to create in mlab database
var PostModel = mongoose.model('posts', PostSchema);

module.exports = PostModel;
