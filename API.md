# API
All api requests are routed through $SERVERIP/api/

## Projects

To get all projects (you are a member of) you use /api/projects/{authtoken}/{username}

If you provide a valid auth token for the username provided, you will get a Json array with all the data related to the projects. If you do not auth properly you will still get an array, it will just be empty. 

To add a project, send a post request to /api/projects with a json object containing a 'name' and 'owner', and 'token' properties. The name is the name of the project you're creating, and the 'owner' is your username, and 'token' is your auth token. 

## Login

To get an auth token (which is required to do most things on the api) you use /api/login/{username}/{password}. If you sucesfully authenticate you'll get a JSON object containing a valid auth token (which is tied to you) and a message. If you fail to auth, you'll recieve the same object but your token will be 'INVALID_AUTH'

At any point you can test if your token is valid with /api/login/validate/{token}/{username}

To register an account with us, send a post request to /api/login/register/{username}/{password} which will return the same object as login (token, message) except invalid registration will result in a 'INVALID_REG' token.