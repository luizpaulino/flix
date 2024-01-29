var db = db.getSiblingDB('streaming');

// Verifica se a coleção 'aggregator' existe
var collectionExists = db.getCollectionNames().includes('aggregator');

if (!collectionExists) {
    // Cria a coleção 'aggregator' se não existir
    db.createCollection('aggregator');
}

// Verifica se já existe um documento com o _id "AGGREGATOR"
var existingAggregator = db.aggregator.findOne({ "_id": "AGGREGATOR" });

if (!existingAggregator) {
    // Insere o documento se não existir
    db.aggregator.insertOne({
        "_id": "AGGREGATOR",
        "totalVideos": 0,
        "totalFavorites": 0,
        "totalWatched": 0
    });
    print('Coleção inicializada com sucesso!');
} else {
    print('Coleção já existe. Nenhuma operação necessária.');
}
