


# uberFlickerDemo:

This application make a query to flickr server and returns a list of photos regarding your query.



## **Features**

 - When you click on the app it returns list of photos with query
   “kittens”.
 - Currently in a single page it returns 20 records. When you scroll it
   down, it make another call of next page and returns next 20 records.
   So you can scroll infinitely till last page.
 - It also cache your images.
 - This application doesn’t use any third party library.
 - This application need write storage permission for Image Caching
   purpose.

## Implementation Detail

 - PhoneListActivity – It is a launcher activity.
 - PhoneListFragment – It is a fragment which list all photos.
 - AdapterPhotoList – It is an Adapter to render photo list to
   recyclerview.
 - NetworkCallAsync – It is an Async Task which make server call.
 - DownloadThread – This is a Thread class which download image from
   server.
