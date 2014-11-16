Gogo logbook
============

What is gogo logbook?
---------------------

Gogo logbook is an electronic log book to log events and agreements. For example you can log agreements made with 
a customer during a meeting. The log book enables you to quickly find back these log messages.
Another advantage of an electronic log book is that other users can add log messages and read the log messages
of each other.

Why  gogo logbook?
------------------

Several other electronic log books exist. ELOG is an example of a web application that is easy to configure.
Unfortunately it is not feasible for each company to install or host such a web application on their intranet.
Gogo logbook is a stand alone application that stores the log book in a single text file. If this text file
is located on a network share then multiple users can work with gogo logbook at the same time.

System requirements
-------------------

Gogo logbook demands little of a computer. Modern computers have more than sufficient amounts of memory and
processor power for gogo logbook.

The only requirement is a Java runtime or Java SDK.
A recent Java runtime is already present on most Windows computers.

Start
-----

To start gogo logbook from a command prompt (Windows) or shell (Linux/Mac) use the following command:

java -jar gogologbook.jar -lang=<language code> <path to log book file>

Supported languages are English (language code en) and Dutch (language code nl).
The path to the log book file can be a file in the file system of the computer or a network share (for example \\computername\directory\logbook.txt)
The log book file may no exist when gogo logbook is started for the first time. 
The log book file is created When the first log message is added.

Since this log book file contains the complete log book it is wise to backup this file regularly.

The parameter -lang=en ensures that the  user interface is in English.  For computers that have English as the
language for the user interface this parameter can be omitted.

Double clicking the jar file does not work because gogo logbook then does not know the path to the log book file.

Create shortcut to start gogo logbook in Windows
------------------------------------------------

For most users it is not easy or handy to start gogo logbook from a command prompt.
A shortcut from the start menu or desktop is much easier. To make a shortcut at the desktop follow these steps:

Start a command prompt and use the command cd (change directory) to the directory containing the jar file.
Execute the following command (where logbook.txt must be replaced by the path where your log book file is located):

echo java -jar gogologbook.jar -lang=nl logbook.txt > logbook.bat

Right click on the desktop and select New and then Shortcut.
Choose the file logbook.bat and enter "gogo logbook" as the name.
