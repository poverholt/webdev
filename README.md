# webdev

A Clojure web app used to study Eric Normand's Purely Functionaly TV Web Development LispCast.

## Usage

"lein run" will run server on port 10443. Visit "https://127.0.0.1:10443". In an environment
like Heroku, the environment will provide env variable PORT.

For security reasons, the project does not provide keystore, cert authority, cert key or
password file. For local development, mkcert, openssl and keytool can be used to generate the
"keystore.jks" JKS file. Put the password in file "pwd.txt". All these files must be
put at root directory of project.

### Options

lein run <port> can be used to override the port number. Note that port 443 is an accepted
number for a production HTTPS/SSL port. Note that 443 is a privileged port and the server
will throw an exception if the server process does not have root access. There are several
ways to solve this, but one way it to run "sudo lein run <port>". In development, start the
repl with "sudo lein repl".

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
