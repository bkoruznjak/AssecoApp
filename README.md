# HashApp
Test website hash buiilder

App for website that takes user input and validates if a website url is valid.

After the validation success the user may read the website contents and generate a SHA1 hash of the website.

If the first byte of the hash is even the hash will be stored in the database, otherwise in the shared preferences.

If the website already exists in the database/shared prefs, the submit button is disabled for 5 seconds.

Info about website URL, SHA1 hash and storage location are updated per submit click.
