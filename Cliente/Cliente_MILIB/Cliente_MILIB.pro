#-------------------------------------------------
#
# Project created by QtCreator 2019-05-22T00:31:20
#
#-------------------------------------------------

QT       += core gui
QT += network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Cliente_MILIB
TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which has been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0


SOURCES += \
        main.cpp \
        mainwindow.cpp \
    ventanaimagen.cpp \
    lectorsintaxis.cpp \
    ServerLibrary/client.cpp \
    ServerLibrary/serverlibrary.cpp \
    json/jsonserializer.cpp

HEADERS += \
        mainwindow.h \
    ventanaimagen.h \
    lectorsintaxis.h \
    ServerLibrary/client.h \
    ServerLibrary/serverlibrary.h \
    json/jsonserializer.h \
    Data_Structures/nodo.hpp \
    Data_Structures/nodo_def.h \
    Data_Structures/lista_def.h \
    Data_Structures/lista.hpp

FORMS += \
        mainwindow.ui \
    ventanaimagen.ui
