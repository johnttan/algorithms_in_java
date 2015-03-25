var fs = require('fs');

var Graph = {
  store: {},
  numEdges: 0,
  addEdge: function(v, w){
    this.store[v] = this.store[v] || [];
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
  var StartDist = {};
  var EndDist = {};

  var dfsQ = [];
  for(var v in Graph.store){
    GraphStore[v] = GraphStore[current] || {
      blue: false,
      red: false
    }
    StartDist[v] = Math.POSITIVE_INFINITY;
    EndDist[v] = Math.POSITIVE_INFINITY;
  }
  nounIndex[nounA].forEach(function(v){
    dfsQ.push(v);
    StartDist[v] = 0;
    GraphStore[v].blue = true;
  });

  while(dfsQ.length > 0){
    var current = dfsQ.unshift();

    Graph.getEdges(current).forEach(function(el){
      if(!GraphStore[el].blue){
        dfsQ.push(el);
        GraphStore[el].blue = true;
        StartDist[v] = Math.Min(StartDist[v], StartDist[current] + 1);
      }
    })
  }
};

loadGraph('wordnet/synsets.txt', 'wordnet/hypernyms.txt');

