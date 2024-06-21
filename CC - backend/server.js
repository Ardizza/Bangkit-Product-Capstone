'use strict';

const Hapi = require('@hapi/hapi');
const { Firestore } = require('@google-cloud/firestore');
const { v4: uuidv4 } = require('uuid');

// Inisialisasi Firestore
const firestore = new Firestore();

const init = async () => {

    const server = Hapi.server({
        port: process.env.PORT || 8080,
        host: '0.0.0.0'
    });

    // Route untuk menambah dokumen
    server.route({
        method: 'POST',
        path: '/add',
        handler: async (request, h) => {
            const { data, weeks } = request.payload;
            const docId = uuidv4();
            const docRef = firestore.collection('karbon').doc(docId);
            await docRef.set({
                id: docId,
                data: data,
                weeks: weeks
            });
            return h.response({ id: docId }).code(201);
        }
    });

    // Route untuk mengambil dokumen
    server.route({
        method: 'GET',
        path: '/get/{docId}',
        handler: async (request, h) => {
            const docId = request.params.docId;
            const docRef = firestore.collection('karbon').doc(docId);
            const doc = await docRef.get();
            if (!doc.exists) {
                return h.response({ error: 'Document not found' }).code(404);
            }
            return h.response(doc.data()).code(200);
        }
    });

    await server.start();
    console.log('Server running on %s', server.info.uri);
};

process.on('unhandledRejection', (err) => {
    console.log(err);
    process.exit(1);
});

init();
