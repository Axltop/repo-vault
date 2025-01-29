import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import {Key} from "@mui/icons-material";

interface BasicTableProps {
    tableData: { id: number; url: string }[] | null;
    deleteRepo: (id:number) => void;
    addSecret: (repoId:number) => void;
}
 const BasicTable: React.FC<BasicTableProps> = ({tableData,deleteRepo,addSecret}) => {

    return (
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>ID#</TableCell>
                        <TableCell align="left">URL</TableCell>
                        <TableCell align="left">Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {tableData?.map((row) => (
                        <TableRow
                            key={row.id}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.id}
                            </TableCell>
                            <TableCell>{row.url}</TableCell>

                            <TableCell>
                                <Button variant="outlined" color={"error"} startIcon={<DeleteIcon />}  onClick={() => deleteRepo(row.id)}>
                                    Delete
                                </Button>
                                <Button variant="outlined" color={"primary"} startIcon={<Key />}  onClick={() => addSecret(row.id)}>
                                    Add Secret
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default BasicTable;