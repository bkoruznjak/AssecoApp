# HashApp
Test website hash buiilder

App that takes website URL adresses as user input and validates if a website url is valid.

After the validation success the user may read the website contents and generate a SHA1 hash of the website.

If the first byte of the hash is even the hash will be stored in the database, otherwise in the shared preferences.

Info about website URL, SHA1 hash and storage location are updated per submit click.
