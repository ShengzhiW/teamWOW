# teamWOW - While One Walk

##### Note: press sync project with graydle files when you pull (fixes some problems)

## QUICK GIT WORKFLOW VERSION
1. git pull
2. make code changes
3. git add filename
4. git commit -m "insert message"
5. git pull
	- If merge conflicts:
	- fix merge conflicts
	- git add filename
	- git commit -m "insert message"
	- git pull
6. git push

### Good git commands to know
- git reset --hard origin/master:
  - This command will reset your local files to whatever is in the git repo
  - WILL UNDO ALL OF YOUR CHANGES
- git status
  - Will tell you what files are untracked, uncommitted changes, files that are
    committed (staged), files that have been modified, etc
  - When in doubt use git status! It will probably tell you what's going on

### Basic Git work flow
When you start working on the project, before you do anything.
1. git status (Check to make sure you don't have any uncommitted changes)
    - If you do make sure to add, and commit them (if you follow this guide
	  you should not have any though)
2. git pull (to get the most recent version of the code)
Add your code changes until you are ready to send your code to the Github
3. git status (Check what files you have modified)
4. git add [fileName]
    - do this for all files you have changed
	  - Be careful with git add . since this will add all files, even untracked
    ones!
5. git commit -m "some commit message"
    - Your changes are ready to be pushed!
      Make your commit messages meaningful. If we need to revert changes it
      will be helpful
6. git pull
    - VERY VERY IMPORTANT!!!!
    - ALWAYS PULL BEFORE YOU PUSH
7. Note - If you have merge errors, git will tell you you have merge conflicts
    in which files. Open those files and you will see where the merge conflicts
    are. It will be wrapped in <<<HEAD ==== >>>>some numbers and letters
    One of them is your changes and one is what is in the Github currently
    Fix the merge conflict
    MAKE SURE TO DELETE THE CONFLICT MARKERS (i.e. the <<< ==== and >>> stuff)
8. If you had to fix merge errors, you have changed the file
    - git add [fileName]
    - git commit -m "some commit message"
    - git pull
    - Repeat until git tells you no conflicts
9. git push
    - Now your changes are pushed to the Github!!!

### Things recently added:
- Navigation bar
- Logout, delete account
- Multiple activity pages
- Basic leaderboard functionality

### Things to implement/work on:
- Cleaning code (split into smaller functions and stuff, move stuff)
- I feel like we should use separate pages as opposed to fragments (??)
- Avatar stuff
- Leaderboards
- Custom login (difficult if using Firebaseauth)
- ((Artifacts))
- Rename the package name lmao

#### Links?? (Add anything here):
- [Group Trello](https://trello.com/cs110teamwow/home)
- [Figma (Screen Mock-ups)](https://www.figma.com/file/Wj2YNnMflZunVORhykRdZ9EE/OneWalk)
- [Firebase Login System](https://firebase.google.com/docs/auth/android/firebaseui)
- [Google Drive](https://drive.google.com/drive/u/0/folders/0AG7pKDOP246NUk9PVA) (May ask you to switch accounts)
