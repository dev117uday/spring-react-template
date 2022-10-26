import { initializeApp } from 'firebase/app';

const firebaseConfig = {
	apiKey:import.meta.env.VITE_firebase_apiKey,
	authDomain:import.meta.env.VITE_firebase_authDomain,
	databaseURL:import.meta.env.VITE_firebase_databaseURL,
	projectId:import.meta.env.VITE_firebase_projectId,
	storageBucket:import.meta.env.VITE_firebase_storageBucket,
	messagingSenderId:import.meta.env.VITE_firebase_messagingSenderId,
	appId:import.meta.env.VITE_firebase_appId
};

let firebaseApp = initializeApp(firebaseConfig);
export default firebaseApp;