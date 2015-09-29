cmput301a1-reflexBuzzer game for CMPUT 301, Software Engineering---Requirements**UML design diagram** demonstrating OO principles**Single user mode** that measures reaction times- When user enters this mode, dialog is presented which explains how to react quickly- When user dismisses dialog, game startsGame waits between 10-2000ms before prompting a click- If user clicks early, game complains and restarts the timer (TODO new random duration?)- Timer is not visibleWhen timer ends, user is prompted to click- Latency between TimerEnd and UserClick is displayed, and persisted (for stats)- The game then restarts- User can quit by pressing Back button (handle onPause properly here)**Gameshow buzzer mode** for friends playing trivia games- When user enters this mode, dialog is presented which asks for # of players (2,3,4 inclusive)- Buzzer screen appears with the appropriate # of buttons *displayed as large as possible*- When a button is pressed, a dialog is presented showing the Player who pressed it (first)- *The Player who pressed the button* and *the # of players* are persisted (for stats)- Dismissing the dialog returns to the buzzer screen**Stats view**- reaction time statistics    - minimum time of all reaction times, the last 10 times, and the last 100 times.    - maximum time of all reaction times, the last 10 times, and the last 100 times.    - average time of all reaction times, the last 10 times, and the last 100 times.    - median time of all reaction times, the last 10 times, and the last 100 times- buzzer counts    - 2 player: Player 1 buzzes, Player 2 buzzes    - 3 player: Player 1 buzzes, Player 2 buzzes, Player 3 buzzes    - 4 player: Player 1 buzzes, Player 2 buzzes, Player 3 buzzes, Player 4 buzzesStatistics must be **clearable** and **emailable**.---AttributionRed Button- http://www.publicdomainpictures.net/view-image.php?image=82878&picture=red-button-for-web- License: Public DomainAndroid Developer Documentation- http://developer.android.comNumber picker dialog- http://stackoverflow.com/a/17806895/1817465Adding a number picker to an AlertDialog with setView- http://stackoverflow.com/a/11800717/1817465Gravity and layout_gravity- http://stackoverflow.com/q/3482742/1817465Set layout dynamically in android- http://stackoverflow.com/questions/17070047/how-to-set-layout-dynamically-in-android