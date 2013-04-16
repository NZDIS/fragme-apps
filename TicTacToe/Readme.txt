This application both contains a desktop and mobile version of the TicTacToe game, both of which are interoperable.

Basic setup description for the Eclipse IDE are provide in the following.

This application requires both 'fragme' and 'jgroups3' in order to run. 

All relevant repositories comply with the following general structure: 

<Repository name>
- src (Source files of repository)
- test (Test files for sources)

However, this structure varies if a desktop and mobile version are involved:

<Repository name>
- desktop
  -- src (Source files for desktop version of application/framework)
  -- test (Test files for desktop version)
- android (folder for Android version)
  -- src (Source files for mobile version of application/framework)
  -- res (Resources files for the Android project)
  - AndroidManifest.xml (Android manifest for application)
- shared
  --src (Source files that are shared between desktop and mobile version)


Example setup for TicTacToe application:

Desktop:

- Create Java Project
- link source folders 'fragme/src', 'jgroups3/src', 'fragme-apps/TicTacToe/Desktop/src' and 'fragme-apps/TicTacToe/Shared/src' into the project.
- Start TictactoeUI.java (under fragme-apps/TicTacToe/Desktop/src/org/nzdis/tictactoeDesk) to run application.

Android:

- Create Android Project
- link source folders 'fragme/src', 'jgroups3/src', 'fragme-apps/TicTacToe/Android/src' and 'fragme-apps/TicTacToe/Shared/src' into the project.
- Copy 'res' folder (fragme-apps/TicTacToe/Android/res) into the project.
- Copy 'AndroidManifext.xml' (fragme-apps/TicTacToe/Android/AndroidManifest.xml) into the project.
- Connect phone (Emulator does not work properly with UDP network packets).
- Build Project and start as Android Application and deploy to phone.

