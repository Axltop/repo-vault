export const API_BASE_URL = "/api";  // Replace with your base API URL

export const API_ENDPOINTS = {
    repo: `${API_BASE_URL}/repositories`,
    secret: {
        validate:`${API_BASE_URL}/secrets/validate`,
        main: `${API_BASE_URL}/secrets`,
        decode:`${API_BASE_URL}/secrets/decode`
    },
};