# teamWOW - While One Walk

### Good git commands to know
- git reset --hard origin/master:
  - This command will reset your local files to whatever is in the git repo 
- git status 
  - When in doubt git status will tell you what files are untracked, uncommited
    changes, files that are committed (staged), files that have been modified, etc
  - When in doubt use git status! It will probably tell you what's going on

### BASIC GIT WORK FLOW
- When you start working on the project, before you do anything
  - 1. git status (Check to make sure you don't have any uncommited changes)
    	- If you do make sure to add, and commit them (if you follow this guide
	  you should not have any though)
  - 2. git pull (to get the most recent version of the code)
- Add your code changes until you are ready to send your code to the github
  - 3. git status (Check what files you have modified)
  - 4. git add [fileName]
    	- do this for all files you have changed
  - 5. git commit -m "some commit message"
    - Your changes are ready to be pushed!
      Make your commit messages meaningful. If we need to revert changes it will be
      helpful
  - 6. git pull
    - VERY VERY IMPORTANT!!!!
    - ALWAYS PULL BEFORE YOU PUSH
  - 7. Note - If you have merge errors, git will tell you you have merge conflicts
    in which files. Open those files and you will see where the merge conflicts
    are. It will look something like this:
    
    <<<<<<<<<HEAD
     SOME CODE HERE
    ===============
    
     SOME MORE CODE HERE
    
    >>>>>>>>>>>>> SOME RANDOM STRING OF NUMBERS AND LETTERS
    
    One of them is your changes and one is what is in the github currently
    Fix the merge conflict
    MAKE SURE TO DELETE THE CONFLICT MARKERS (ie the <<< ==== and >>> stuff)
  - 8. If you had to fix merge errors, you have changed the file
    	- git add [fileName]
	- git commit -m "some commit message"
	- git pull 
       Repeat until git tells you no conflicts
  - 9. git push 
    	- Now your changes are pushed to the github!!!

## QUICK GIT WORKFLOW VERSION
1. git pull
2. make code changes
3. git add filename
4. git commit -m "insert message"
5. git pull
	- If merge conflicts
	  6.a fix merge conflicts
	  6.b git add filename
	  6.c git commit -m "insert message"
	  6.d git pull
7. git push

### Things recently added:
- Pedometer working
- Each user account has individual step count (see the firebase database)

### Things to implement on soon:
- Refactoring code (split into multiple activities / services ??)
- Each user account should be able to read their step count back from the databaseinstead of it resetting every time.
- Navigation bar
- Leaderboards
- 


