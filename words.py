import os
import requests
from bs4 import BeautifulSoup

hardwordending = "-hardwords.txt"
storylineending = "-storyline.txt"
simpletxtending = ".txt"

def replaceForJson(string):
    return string.replace("\"", "\\\"").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r").replace("\b","\\b").replace("\f", "\\f").replace("'", "\'").replace("’", "\'").replace("‘", "\'")

def makeJson(author, title, genre, level, content, storyline, hardwords):
    json = """{
        "author":"%s",
        "title":"%s",
        "genre":"%s",
        "level": "%s",
        "storyline":"%s",
        "hardwords":%s,
        "content":%s
    },""" % (author, title, genre, level, storyline, hardwords, content)
    return json

#TODO: birden fazla "-" var ise 2.cisini al
def getAuthor(fileName):
    titleAuthor = fileName.split("-")
    if fileName.count("-") == 2:
        author = (titleAuthor[2]).replace("_", " ")
    else:
        author = (titleAuthor[1]).replace("_", " ")
    return author

def getTitle(fileName):
    titleAuthor = fileName.split("-")
    if fileName.count("-") == 2:
        title = titleAuthor[0].replace("_", " ")
    else:
        title = (titleAuthor[0] + titleAuthor[1]).replace("_", " ")
    return title

def getHardWordsJson(hardwords):   
    words = hardwords.split(",")
    wordsJson = "["
    for word in words:
        wordsJson += "\"%s\"," % word.strip()
    wordsJson = wordsJson.strip(",") + "]"
    return wordsJson

def getStorylineContentJson(content):
        return replaceForJson(content)

#TODO: make it so it returns chapter contents
def getContent(fileName):
    with open(fileName) as f:
        chaptercount = 0
        chapters = list()
        for line in f.readlines():
            l = line.strip()
            if l != "":
                chapters[chaptercount] += l
                
            if "chapter" in l.lower() and (len(l.split()) == 2 or len(l.split() == 3)):
                chaptercount += 1
            
        #TODO: return array of contents
        

sonuc = ""
files = os.listdir(".")
for f in files:
    if f.endswith(".txt"):
        corefilename = f[:f.index(".txt")]
        
        corewebname = corefilename.lower().replace("_", "-").replace(",", "-")
        source = requests.get("https://english-e-reader.net/book/" + corewebname).content
        soup = BeautifulSoup(source, 'html.parser')
        print(corewebname)
        ps = soup.find_all("p")
        div = soup.find("div", id="hard-words")
        divs = div.find_all("div")

        level = ps[1].string.strip()
        genre = ps[2].string.strip()

        #TODO: UNICODE problem for '
        storyline = ps[4].string.strip()
        storylineJson = getStorylineContentJson(storyline)
        hardwords = divs[1].string.strip()
        hardwordsJson = getHardWordsJson(hardwords)
        content = getContent(f)
        title = getTitle(corefilename)
        author = getAuthor(corefilename)

        if level.lower() == "elementary":
            os.rename(f, "elementary\\" + f)
        elif level.lower() == "pre-intermediate":
            os.rename(f, "pre-intermediate\\" + f)
        elif level.lower() == "intermediate":
            os.rename(f, "intermediate\\" + f)
        elif level.lower() == "upper-intermediate":
            os.rename(f, "upper-intermediate\\" + f)
        elif level.lower() == "advanced":
            os.rename(f, "advanced\\" + f)
        elif level.lower() == "unabridged":
            os.rename(f, "unabridged\\" + f)
        
        sonuc += makeJson(author, title, genre, level, content, storylineJson, hardwordsJson)

with open("sonuclar.json", "w") as f:
    sonuc =  "{ \"books\":[" + sonuc.strip().rstrip(",") + "]}"
    f.write(sonuc)
print(sonuc)
        

