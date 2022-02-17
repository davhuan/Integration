# Team Tiger 2019 - Systemintegration, ISGC15

### [Läs mer på Wiki](https://bitbucket.org/bennchri/2019-tiger/wiki/Home)


---

git branch (se vilka branches som finns)

git checkout master (byter till master branch)

git check out testing (byter till testing branch)

git add . (lägger till allt för commit)

git commit -m "meddelande"

git push origin BRANCH (ex. master då git push origin master)

git pull origin BRANCH (hämtar då istället från bitbucket)

git status (kolla hur det ligger till på din dator jämfört med bitbucket)

git reset --hard (om något går åt skogen eller buggar, kör detta sen en git pull på den branch du vill ha)

###**ANVÄND FORKS**

---

Procedur för hämtning/pushning:

1. git pull origin master eller testing

2. Skriv/arbete

3. Spara allt i Eclipse först

4. git add .

5. git commit -m "skriv ett bra meddelande"

6. git push origin master eller testing

---

Procedur för installation av repo på dator:

1. Skapa en mapp för allt

2. Gå in där, kör igång git bash

3. Kör kommandot: git clone https://DITT_ANVÄNDARNAMN@bitbucket.org/bennchri/2019-tiger.git

4. Använde du verkligen ditt användarnamn i git clone?

---

Procedur för taggning (versionshantering), **använd inte denna lösning förresten**

1. Ändra i programmet, spara

2. git add .

3. git commit -m "blah"

4. git tag v0.5 (eller 1.0 eller vad det nu är för version, kolla bitbucket först vilken som var senast)

5. git push origin testing v0.5 (samma tagg-namn som i steg 4)

---

$ cd workspace/2019-tiger				
$ git pull origin master					
$ git checkout testing 					
$ git fetch && git checkout namn	
$ git branch					
$ git status 						
$ git add .
$ git commit -m “text”
$ git push
$ git push --set-upstream origin testing 	

---

$ git checkout master

(this will delete all your local changes to master)

$ git reset --hard upstream/master

(take care, this will delete all your changes on your forked master)

$ git push origin master --force


