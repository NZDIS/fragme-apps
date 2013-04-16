fragme-apps
===========

FragMe applications

All FragMe-related repositories comply with the following general structure: 

<Repository name>
- src (Source files of repository)
- test (Test files for sources)

This structure varies if a desktop and mobile (Android) version are involved:

<Repository name>
- desktop
  - src (Source files for desktop version of application/framework)
  - test (Test files for desktop version)
- android (folder for Android version)
  - src (Source files for mobile version of application/framework)
  - test (Test files for mobile version) (optional)
  - res (Resources files for the Android project)
  - AndroidManifest.xml (Android manifest for application)
- shared
  - src (Source files that are shared between desktop and mobile version) (optional)

To get started with FragMe have a look at the TicTacToe game.
Its full setup instructions (which serve as an example for all FragMe applications in Eclipse) can be found under 'fragme-apps/TicTacToe/Readme.txt'. A more detailed setup guide can be found under 'fragme-apps/TicTacToe/user guide'.

Note: Boom and RoboGame (developed in 2004) have not yet been ported to the current FragMe version and are saved here for archiving purposes.