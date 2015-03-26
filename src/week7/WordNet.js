var fs = require('fs');

var MakeGraph = function(){
  return {
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
  }
};
var Graph;
var nounIndex;
var synIndex;

function loadGraph(syns, hypers){
  Graph = MakeGraph();
  nounIndex = Object.create(null);
  synIndex = Object.create(null);

  var synsFile = fs.readFileSync(syns, {encoding: 'utf8'}).split('\n');
  synsFile.forEach(function(el){
    el = el.split(',');
    if(el.length > 1){
      var splitNouns = el[1].split(' ');
      synIndex[el[0]] = el[1];

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
    GraphStore[v] = {
      blue: false,
      red: false,
      secondVisit: false,
      startDist: Number.POSITIVE_INFINITY,
      endDist: Number.POSITIVE_INFINITY,
      ancestorCount: 0
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
  var parents = Object.create(null);

  nounIndex[nounB].forEach(function(v){
    dfsQ.push(v);
    GraphStore[v].endDist = 0;
    if(GraphStore[v].blue){
      GraphStore[v].red = true;
    }
  });
  while(dfsQ.length > 0){
    var current = dfsQ.shift();
    parents[current] = parents[current] || [];
    Graph.getEdges(current).forEach(function(el){
      // parents[current].push(el);
      var V = GraphStore[el];
      if(!V.secondVisit){
        dfsQ.push(el);
        V.secondVisit = true;
        V.endDist = Math.min(V.endDist, GraphStore[current].endDist + 1);
        if(V.blue){
          V.red = true;
          // console.log('marking ancestor', Graph.getEdges(el)[i], el, GraphStore[el].red, GraphStore[el].blue);
          GraphStore[el].ancestorCount ++;
        }
      }
    })
  };

  var minDist = Number.POSITIVE_INFINITY;
  var minAncestor;
  for(var V in GraphStore){
    var path = GraphStore[V].endDist + GraphStore[V].startDist;
    if(GraphStore[V].red){
      // console.log(path, V, GraphStore[V], Graph.getEdges(V));
    }
    if(GraphStore[V].red && GraphStore[V].ancestorCount === 0 && path < minDist){
      minAncestor = V;
      minDist = path;
    }
  }
  // console.log(Graph.getEdges(minAncestor), nounIndex['a'], nounIndex['b'], Graph.getEdges('0'), Graph.getEdges('1'));
  // console.log(minDist, minAncestor, synIndex[minAncestor]);

  return minDist;
};

loadGraph('wordnet/synsets.txt', 'wordnet/hypernyms.txt');
getDistance('mebibit', 'Ascension');
// getDistance('white_marlin', 'mileage');
// getDistance('individual', 'edible_fruit');
// loadGraph('wordnet/synsets15.txt', 'wordnet/hypernyms15Path.txt');
// getDistance('a', 'b');
