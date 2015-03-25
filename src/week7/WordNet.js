var fs = require('fs');

var Graph = {
  store: {},
  numEdges: 0,
  addEdge: function(v, w){
    this.store[v] = this.store[v] || [];
    this.store[w] = this.store[w] || [];
    this.store[v].push(w);
    this.numEdges ++;
  },
  getEdges: function(v){
    return this.store[v];
  }
};

var nounIndex = Object.create(null);

function loadGraph(syns, hypers){
  var synsFile = fs.readFileSync(syns, {encoding: 'utf8'}).split('\n');
  synsFile.forEach(function(el){
    el = el.split(',');
    if(el.length > 1){
      var splitNouns = el[1].split(' ');
      splitNouns.forEach(function(noun){
        if(nounIndex[noun] === undefined){
          nounIndex[noun] = [];
        }
        nounIndex[noun].push(el[0]);
      })
    }
  })
  var hypersFile = fs.readFileSync(hypers, {encoding: 'utf8'}).split('\n');

  hypersFile.forEach(function(el){
    el = el.split(',');
    for(var i=1;i<el.length;i++){
      Graph.addEdge(el[0], el[i]);
    }
  })
};

function getDistance(nounA, nounB){
  var GraphStore = {};

  var dfsQ = [];
  for(var v in Graph.store){
    GraphStore[v] = GraphStore[current] || {
      blue: false,
      red: false,
      secondVisit: false,
      startDist: Math.POSITIVE_INFINITY,
      endDist: Math.POSITIVE_INFINITY
    }
  }
  nounIndex[nounA].forEach(function(v){
    dfsQ.push(v);
    GraphStore[v].blue = true;
    GraphStore[v].startDist = 0;
  });

  while(dfsQ.length > 0){
    var current = dfsQ.shift();

    Graph.getEdges(current).forEach(function(el){
      var V = GraphStore[el];
      if(!V.blue){
        dfsQ.push(el);
        V.blue = true;
        V.startDist = Math.min(V.startDist, GraphStore[current].startDist + 1);
      }
    })
  };

  while(dfsQ.length > 0){
    var current = dfsQ.shift();

    Graph.getEdges(current).forEach(function(el){
      var V = GraphStore[el];
      if(!V.secondVisit){
        dfsQ.push(el);
        V.secondVisit = true;
        V.endDist = Math.min(V.endDist, GraphStore[current].endDist + 1);
        if(V.blue){
          V.red = true;
        }
      }
    })
  };
};

loadGraph('wordnet/synsets.txt', 'wordnet/hypernyms.txt');
getDistance('mebibit', 'Ascension');

