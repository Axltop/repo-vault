import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button, Container, Grid2, TextField} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import {Add, Check, Key, Visibility, VisibilityOff} from "@mui/icons-material";
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import Typography from '@mui/material/Typography';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import {API_ENDPOINTS} from "../const/endpoints";

interface BasicTableProps {
    tableData: { id: number; url: string, secrets: { id: number, secret: string }[] }[] | null;
    deleteRepo: (id: number) => void;
    addSecret: (repoId: number, secret: string) => void;
    deleteSecret: (id: number) => void;
    validateSecret: (id: number, secret: string) => void;
}

const BasicTable: React.FC<BasicTableProps> = ({tableData, deleteRepo, addSecret, deleteSecret, validateSecret}) => {

    return (
        <TableContainer component={Paper}>
            <Table aria-label="collapsible table">
                <TableHead>
                    <TableRow>
                        <TableCell>ID#</TableCell>
                        <TableCell align="left">URL</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {tableData && tableData.map((row) => (
                        <Row key={row.id} row={row} addSecret={addSecret} deleteRepo={deleteRepo}
                             deleteSecret={deleteSecret} validateSecret={validateSecret}/>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default BasicTable;

function Row(props: {
    row: { id: number, url: string; secrets: { id: number, secret: string } [] },
    deleteRepo: (id: number) => void,
    addSecret: (repoId: number, secret: string) => void,
    deleteSecret: (id: number) => void,
    validateSecret: (repoId: number, secret: string) => void;
}) {
    const {row} = props;
    const [open, setOpen] = React.useState(false);
    const [formErorrs, setFormErrors] = React.useState<Array<{ field: string, message: string }> | null>(null);
    const [formData, setFormData] = React.useState<{ secret: string } | null>({secret: ""});

    const [decodedSecret, setDecodedSecret] = React.useState<{ id: number; secret: string } | null>(null);

    const handleChangeSecret = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            secret: e.target.value
        });
    }

    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        var errors: Array<{ field: string, message: string }> | null;
        errors = validateForm(formData || {secret: ""});

        if (isValidForm(errors)) {
            props.addSecret(row.id, formData?.secret || "");
            setFormData({secret: ""});
            setFormErrors(null);
        } else {
            setFormErrors(errors);
        }
    }


    const validateForm = (formData: { secret: string } | null) => {
        var errorsArr = [];

        if (!formData?.secret || formData.secret.length < 1) {
            errorsArr.push({field: "secret", message: "Secret is required."});
        } else if (formData.secret.length < 8) {
            errorsArr.push({field: "secret", message: "Secret must be at least 8 characters."});
        }

        return errorsArr
    }

    const isValidForm = (errors: Array<{ field: string, message: string }> | null): boolean => {
        return errors?.length === 0;
    }

    const handleVisibilityOn = async (id: number) => {

        const payload = {id}
        await fetch(`${API_ENDPOINTS.secret.decode}`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        })
            .then((response) => response.json())
            .then((fetchedData) => {
                setDecodedSecret(fetchedData)
            })
    }
    const handleVisibilityOff = () => {
        setDecodedSecret(null);
    }

    const handleValidateSecret = (event: React.FormEvent) => {
            event.preventDefault();
            const errorsArr = validateForm(formData || {secret: ""});
            if(isValidForm(errorsArr)){
                props.validateSecret(row.id, formData?.secret || "")
                setFormErrors(null);
            }else {
             setFormErrors(errorsArr)
            }

    }

    return (
        <React.Fragment>
            <TableRow
                key={row.id}
                sx={{'&:last-child td, &:last-child th': {border: 0}}}
            >
                <TableCell component="th" scope="row">
                    {row.id}
                </TableCell>
                <TableCell>{row.url}</TableCell>

                <TableCell align={"right"}>
                    <Button variant="contained" color={"error"}
                            onClick={() => props.deleteRepo(row.id)}>
                        <DeleteIcon/>
                    </Button>
                    &nbsp;
                    <Button
                        color={"primary"}
                        variant={"contained"}
                        aria-label="expand row"
                        onClick={() => setOpen(!open)}
                    >
                        {open ? <React.Fragment> <Key/><KeyboardArrowUpIcon/></React.Fragment> :
                            <React.Fragment><Key/><KeyboardArrowDownIcon/></React.Fragment>}
                    </Button>
                </TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Container>

                            <Box sx={{margin: 1}}>
                                <Grid2 container direction="row"
                                       spacing={4} sx={{alignItems: "flex-end", justifyContent: "space-between"}}>
                                    <Grid2>
                                        <Typography variant="h6" gutterBottom component="div">
                                            Secrets
                                        </Typography>
                                    </Grid2>
                                    <Grid2>
                                        <form onSubmit={(event) => {
                                            event.preventDefault()
                                        }}>
                                            <TextField required={true}
                                                       value={formData?.secret}
                                                       size={"small"}
                                                       variant={"outlined"}
                                                       label={"Secret"}
                                                       error={formErorrs?.some(e => e.field === "secret")}
                                                       helperText={formErorrs?.find(e => e.field === "secret")?.message}
                                                       onChange={handleChangeSecret}/>
                                            &nbsp;
                                            <Button variant={"outlined"} onClick={handleClick} ><Add/></Button>
                                            &nbsp;
                                            <Button variant={"contained"} color={"primary"}
                                                    onClick={handleValidateSecret}><Check/></Button>

                                        </form>
                                    </Grid2>
                                </Grid2>
                                <Table size="small" aria-label="secrets">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Id</TableCell>
                                            <TableCell>Secret</TableCell>
                                            <TableCell align="right">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {row.secrets && row?.secrets.map((secret) => (
                                            <TableRow key={secret.id}>
                                                <TableCell component="th" scope="row">
                                                    {secret.id}
                                                </TableCell>
                                                <TableCell component="th" scope="row">
                                                    <TextField
                                                        type={secret.id === decodedSecret?.id ? "text" : "password"}
                                                        size={"small"}
                                                        value={secret.id === decodedSecret?.id ? decodedSecret.secret : secret.secret}
                                                        disabled={true}
                                                    />

                                                    {secret.id !== decodedSecret?.id ?
                                                        <Button variant={"contained"} color={"primary"}
                                                                onClick={() => handleVisibilityOn(secret.id)}><VisibilityOff/></Button>

                                                        :

                                                        <Button variant={"contained"} color={"primary"}
                                                                onClick={() => handleVisibilityOff()}>
                                                            <Visibility/></Button>
                                                    }
                                                </TableCell>
                                                <TableCell component="th" scope="row" align={"right"}>
                                                    <Button variant={"contained"} color={"error"}
                                                            onClick={() => props.deleteSecret(secret.id)}><DeleteIcon/></Button>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </Box>
                        </Container>
                    </Collapse>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );

}
