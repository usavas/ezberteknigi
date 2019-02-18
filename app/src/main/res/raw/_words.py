import os
import requests
from bs4 import BeautifulSoup

hardwordending = "-hardwords.txt"
storylineending = "-storyline.txt"
simpletxtending = ".txt"

def makeMultipleNewlinesSingle(string):
    while "\n\n" in string:
        string = string.replace("\n\n", "\n")
    return string

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
        title = (titleAuthor[0] + titleAuthor[1]).replace("_", " ")
    else:
        title = titleAuthor[0].replace("_", " ")
    return title

def getHardWordsJson(hardwords):   
    words = hardwords.split(",")
    wordsJson = "["
    for word in words:
        wordsJson += "\"%s\"," % word.strip().strip(".")
    wordsJson = wordsJson.strip(",") + "]"
    return wordsJson

def getStorylineContentJson(content):
        return replaceForJson(content)

#TODO: make it so it returns chapter contents
def getChapters(fileName):
    with open(fileName) as f:
        chapters = dict()       
        chapter = ""
        chapterHeader = ""
        prevChapterHeader = ""

        for line in f.readlines():            
            # l = line.strip()
            if "chapter" in line.lower() and (len(line.split()) == 2 or len(line.split()) == 3):
                prevChapterHeader = line.strip()
                if chapter.strip() != "":
                    if chapterHeader == "":
                        chapters[prevChapterHeader] = chapter
                    else:
                        chapters[chapterHeader] = chapter
                chapterHeader = line.strip()
                chapter = ""

            elif line != "":
                chapter += line

        chaptersInJson = "["
        for key, value in chapters.items():
            chaptersInJson += "{\"%s\": \"%s\"}," % (key, replaceForJson(makeMultipleNewlinesSingle(value.strip())))
        chaptersInJson = chaptersInJson.strip(",") + "]"
        return chaptersInJson
        
def makeDirIfNotExists(dirName):
    if os.path.isdir(dirName) == False:
        os.mkdir(dirName)

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

        storyline = ps[4].string.strip()
        storylineJson = getStorylineContentJson(storyline)
        hardwords = divs[1].string.strip()
        hardwordsJson = getHardWordsJson(hardwords)
        content = getChapters(f)
        print(corefilename)
        title = getTitle(corefilename)
        author = getAuthor(corefilename)

        _ELEMENTARY = "elementary"
        _PREINTERMEDIATE = "pre-intermediate"
        _INTERMEDIATE = "intermediate"
        _UPPERINTERMEDIATE = "upper-intermediate"
        _ADVANCED = "advanced"
        _UNABRIDGED = "unabridged"


        if level.lower() == _ELEMENTARY:
            makeDirIfNotExists(_ELEMENTARY)
            os.rename(f, _ELEMENTARY + "\\" + f)
        elif level.lower() == _PREINTERMEDIATE:
            makeDirIfNotExists(_PREINTERMEDIATE)
            os.rename(f, _PREINTERMEDIATE + "\\" + f)
        elif level.lower() == _INTERMEDIATE:
            makeDirIfNotExists(_INTERMEDIATE)
            os.rename(f, _INTERMEDIATE + "\\" + f)
        elif level.lower() == _UPPERINTERMEDIATE:
            makeDirIfNotExists(_UPPERINTERMEDIATE)
            os.rename(f, _UPPERINTERMEDIATE + "\\" + f)
        elif level.lower() == _ADVANCED:
            makeDirIfNotExists(_ADVANCED)
            os.rename(f, _ADVANCED + "\\" + f)
        elif level.lower() == _UNABRIDGED:
            makeDirIfNotExists(_UNABRIDGED)
            os.rename(f, _UNABRIDGED + "\\" + f)
        
        sonuc += makeJson(author, title, genre, level, content, storylineJson, hardwordsJson)

with open("sonuclar.json", "w") as f:
    sonuc =  "{ \"books\":[" + sonuc.strip().rstrip(",") + "]}"
    f.write(sonuc)
print(sonuc)
        

