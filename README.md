# Team Tiger 2019 - Systemintegration, ISGC15

### [L�s mer p� Wiki](https://bitbucket.org/bennchri/2019-tiger/wiki/Home)


---

git branch (se vilka branches som finns)

git checkout master (byter till master branch)

git check out testing (byter till testing branch)

git add . (l�gger till allt f�r commit)

git commit -m "meddelande"

git push origin BRANCH (ex. master d� git push origin master)

git pull origin BRANCH (h�mtar d� ist�llet fr�n bitbucket)

git status (kolla hur det ligger till p� din dator j�mf�rt med bitbucket)

git reset --hard (om n�got g�r �t skogen eller buggar, k�r detta sen en git pull p� den branch du vill ha)

###**ANV�ND FORKS**

---

Procedur f�r h�mtning/pushning:

1. git pull origin master eller testing

2. Skriv/arbete

3. Spara allt i Eclipse f�rst

4. git add .

5. git commit -m "skriv ett bra meddelande"

6. git push origin master eller testing

---

Procedur f�r installation av repo p� dator:

1. Skapa en mapp f�r allt

2. G� in d�r, k�r ig�ng git bash

3. K�r kommandot: git clone https://DITT_ANV�NDARNAMN@bitbucket.org/bennchri/2019-tiger.git

4. Anv�nde du verkligen ditt anv�ndarnamn i git clone?

---

Procedur f�r taggning (versionshantering), **anv�nd inte denna l�sning f�rresten**

1. �ndra i programmet, spara

2. git add .

3. git commit -m "blah"

4. git tag v0.5 (eller 1.0 eller vad det nu �r f�r version, kolla bitbucket f�rst vilken som var senast)

5. git push origin testing v0.5 (samma tagg-namn som i steg 4)

---

$ cd workspace/2019-tiger				
$ git pull origin master					
$ git checkout testing 					
$ git fetch && git checkout namn	
$ git branch					
$ git status 						
$ git add .
$ git commit -m �text�
$ git push
$ git push --set-upstream origin testing 	

---

$ git checkout master

(this will delete all your local changes to master)

$ git reset --hard upstream/master

(take care, this will delete all your changes on your forked master)

$ git push origin master --force


