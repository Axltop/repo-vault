import React, {useEffect, useState} from 'react';
import './../styles/App.css';
import BasicTable from './mui-table';
import Divider from '@mui/material/Divider';
import MuiRepoForm from "./mui-repo-form";
import {API_ENDPOINTS} from "../const/endpoints";
import SnackbarWrapper from "./mui-snackbar-wrapper";
import Box from "@mui/material/Box";
import {AppBar, Container, CssBaseline, Toolbar} from "@mui/material";
import Typography from "@mui/material/Typography";

export default function App() {

    const [repoData, setRepoData] = React.useState<{
        id: number;
        url: string,
        secrets: { id: number, secret: string }[]
    }[] | null>(null);
    const [snackbars, setSnackbars] = useState<
        {
            message: string;
            severity: 'success' | 'error' | 'info' | 'warning';
            open: boolean,
            duration?: number | null,
            position?: string | null
        }[]
    >([]);

    const handleSubmitForm = async (data: { url: string } | null) => {
        await fetch(API_ENDPOINTS.repo, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data),
        })
            .then(async response => {
                if (!response.ok) {
                    // Extract error message from response JSON
                    const errorData = await response.json();
                    throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then((fetchedData) => {
                setRepoData([...(repoData || []), fetchedData])
            })
            .then(() => addSnackbar('Repo added successfully', 'success', 6000))
            .catch((error) => {
                console.log(error);
                addSnackbar(error.message, 'error', null)
            })


    }
    const getRepos = async () => {
        fetch(API_ENDPOINTS.repo)
            .then(response => response.json())
            .then((fetchedData) => setRepoData(fetchedData));
    }

    useEffect(() => {
        getRepos()
    }, []);

    // Function to handle adding a Snackbar
    const addSnackbar = (message: string, severity: 'success' | 'error' | 'info' | 'warning', duration?: number | null) => {
        setSnackbars([
            ...snackbars,
            {message, severity, open: true, duration},
        ]);
    };

    // Function to handle closing a Snackbar
    const handleCloseSnackbar = (index: number) => {
        const updatedSnackbars = [...snackbars];
        updatedSnackbars[index].open = false;
        setSnackbars(updatedSnackbars);
    };

    const deleteRepo = async (id: number) => {
        await fetch(`${API_ENDPOINTS.repo}/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then((fetchedData) => {
                getRepos()
            })
            .then(() => addSnackbar('Repo deleted successfully', 'success', 6000))
            .catch((error) => {
                addSnackbar(error.message, 'error', null)
            })
    }
    const deleteSecret = async (id: number) => {
        await fetch(`${API_ENDPOINTS.secret.main}/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then((fetchedData) => {
                getRepos()
            })
            .then(() => addSnackbar('Secret deleted successfully', 'success', 6000))
            .catch((error) => {
                addSnackbar(error.message, 'error', null)
            })
    }

    const addSecret = async (repoId: number, secret: string) => {

        const payload = {repoId, secret}
        await fetch(`${API_ENDPOINTS.secret.main}`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        })
            .then((fetchedData) => {
                getRepos()
            })
            .then(() => addSnackbar('Secret saved successfully', 'success', 6000))
            .catch((error) => {
                addSnackbar(error.message, 'error', null)
            })
    }

    const validateSecret = async (repoId: number, secret: string) => {

        const payload = {repoId, secret}
        await fetch(`${API_ENDPOINTS.secret.validate}`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        })
            .then((response ) => response.text())
            .then((fetchedData) => {
                if(fetchedData === "false"){
                    throw new Error("Secret is incorrect.");
                }
                addSnackbar('Secret is correct.', 'success', 6000)

            })
            .catch((error) => {
                addSnackbar(error.message, 'error', null)
            })
    }

    return (


            <Box component="main"
                 sx={{flexGrow: 1, p: 3, transition: "margin 0.3s"}}>

                <Container>
                    <AppBar position="static" >

                        <Typography variant={"h2"} align={"center"} component={"div"}>
                            Vault Service
                        </Typography>

                    </AppBar>
                    <MuiRepoForm handleSubmitForm={handleSubmitForm}/>
                    <Divider/>
                    <BasicTable tableData={repoData ? repoData : null} deleteRepo={deleteRepo} addSecret={addSecret}
                                deleteSecret={deleteSecret} validateSecret={validateSecret}/>
                    <SnackbarWrapper snackbars={snackbars} handleCloseSnackbar={handleCloseSnackbar}/>
                </Container>

            </Box>

    );
}
