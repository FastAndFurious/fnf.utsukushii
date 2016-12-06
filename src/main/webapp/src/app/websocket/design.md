### Design considerations

Angular Services are Singletons. Thus, to support different socket channels in a single application
, we can either use a service that maintains a map of endpoints or define as many classes, deriving from 
a common super class. The base classes then are represented by those singleton instances, 
actually differing only by the servlet path part of the websocket url.
