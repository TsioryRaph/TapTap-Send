<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Tableau de bord - TapTapSend'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root {
            --sidebar-width: 280px;
            --primary-color: #4361ee;
            --success-color: #2ecc71;
            --info-color: #00b4d8;
            --dark-color: #212529;
            --light-color: #f8f9fa;
            --sidebar-bg: #1a1a2e;
            --card-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --transition: all 0.3s ease;
        }

        body {
            padding-top: 60px;
            background-color: #f5f7fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        /* Navigation Top */
        .navbar {
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            background-color: var(--dark-color) !important;
        }

        .navbar-brand {
            font-weight: 600;
            font-size: 1.25rem;
        }

        /* Sidebar */
        #sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            padding-top: 70px;
            background-color: var(--sidebar-bg);
            transition: var(--transition);
            z-index: 1000;
            box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
        }

        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 0.75rem 1.5rem;
            margin: 0.25rem 1rem;
            border-radius: 6px;
            font-weight: 500;
            transition: var(--transition);
        }

        .sidebar .nav-link:hover {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
        }

        .sidebar .nav-link.active {
            background-color: var(--primary-color);
            color: white;
        }

        .sidebar .nav-link i {
            width: 24px;
            text-align: center;
            margin-right: 10px;
        }

        /* Main Content */
        main {
            margin-left: var(--sidebar-width);
            padding: 2rem;
            transition: var(--transition);
        }

        .page-header {
            border-bottom: 1px solid #e9ecef;
            padding-bottom: 1rem;
            margin-bottom: 2rem;
        }

        /* Cards */
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: var(--card-shadow);
            transition: var(--transition);
            overflow: hidden;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background-color: white;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
            font-weight: 600;
        }

        /* Stat Cards */
        .stat-card {
            border-left: 4px solid var(--primary-color);
            transition: var(--transition);
        }

        .stat-card .card-body {
            padding: 1.5rem;
        }

        .stat-card h6 {
            font-size: 0.85rem;
            letter-spacing: 0.5px;
            color: #6c757d;
        }

        .stat-card h2 {
            font-weight: 700;
            color: #343a40;
        }

        .stat-card .icon-wrapper {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
        }

        .stat-card.success {
            border-left-color: var(--success-color);
        }

        .stat-card.success .icon-wrapper {
            background-color: rgba(46, 204, 113, 0.1);
            color: var(--success-color);
        }

        .stat-card.info {
            border-left-color: var(--info-color);
        }

        .stat-card.info .icon-wrapper {
            background-color: rgba(0, 180, 216, 0.1);
            color: var(--info-color);
        }

        /* Tables */
        .table {
            margin-bottom: 0;
        }

        .table thead th {
            border-top: none;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.75rem;
            letter-spacing: 0.5px;
            color: #6c757d;
        }

        .table-hover tbody tr:hover {
            background-color: rgba(0, 0, 0, 0.02);
        }

        /* Badges */
        .badge {
            padding: 0.35em 0.65em;
            font-weight: 500;
            letter-spacing: 0.5px;
        }

        /* Buttons */
        .btn {
            font-weight: 500;
            padding: 0.375rem 0.75rem;
        }

        .btn-sm {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }

        /* Responsive */
        @media (max-width: 992px) {
            #sidebar {
                margin-left: -var(--sidebar-width);
            }

            main {
                margin-left: 0;
            }

            #sidebar.active {
                margin-left: 0;
            }
        }

        /* Chart Placeholder */
        .chart-placeholder {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100%;
            color: #adb5bd;
        }

        .chart-placeholder i {
            font-size: 3rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }
    </style>
</head>
<body>
    <!-- Navigation Top -->
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-speedometer2 me-2"></i>TapTapSend
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarCollapse">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav id="sidebar" class="col-lg-2 d-lg-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                                <i class="bi bi-speedometer2"></i>Tableau de bord
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/envoyer">
                                <i class="bi bi-send"></i>Envoyers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/client">
                                <i class="bi bi-people"></i>Clients
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/parametres">
                                <i class="bi bi-gear"></i>Paramètres
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-lg-10 px-lg-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 page-header">
                    <h1 class="h2 mb-0">Tableau de bord</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="bi bi-share"></i> Partager
                            </button>
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="bi bi-download"></i> Exporter
                            </button>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown">
                                <i class="bi bi-calendar3"></i> Cette semaine
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
                                <li><a class="dropdown-item" href="#">Cette semaine</a></li>
                                <li><a class="dropdown-item" href="#">Ce mois</a></li>
                                <li><a class="dropdown-item" href="#">Cette année</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Stats Cards -->
                <div class="row mb-4 g-4">
                    <div class="col-md-4">
                        <div class="card stat-card h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Total des frais</h6>
                                        <h2 class="mb-0">${not empty totalFrais ? totalFrais : '0'} €</h2>
                                    </div>
                                    <div class="col-auto">
                                        <div class="icon-wrapper bg-primary text-white">
                                            <i class="bi bi-currency-euro"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card stat-card success h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Envoyers ce mois</h6>
                                        <h2 class="mb-0">24</h2>
                                    </div>
                                    <div class="col-auto">
                                        <div class="icon-wrapper">
                                            <i class="bi bi-send"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card stat-card info h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Clients actifs</h6>
                                        <h2 class="mb-0">15</h2>
                                    </div>
                                    <div class="col-auto">
                                        <div class="icon-wrapper">
                                            <i class="bi bi-people-fill"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts and Tables -->
                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="card h-100">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">Activité récente</h5>
                                <a href="${pageContext.request.contextPath}/envoyer" class="btn btn-sm btn-primary">
                                    <i class="bi bi-plus"></i> Nouveau transfert
                                </a>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Client</th>
                                                <th>Montant</th>
                                                <th>Date</th>
                                                <th>Statut</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>#12345</td>
                                                <td>Jean Dupont</td>
                                                <td>150.00 €</td>
                                                <td>26/05/2025</td>
                                                <td><span class="badge bg-success">Complété</span></td>
                                            </tr>
                                            <tr>
                                                <td>#12344</td>
                                                <td>Marie Martin</td>
                                                <td>89.50 €</td>
                                                <td>26/05/2025</td>
                                                <td><span class="badge bg-success">Complété</span></td>
                                            </tr>
                                            <tr>
                                                <td>#12343</td>
                                                <td>Pierre Durand</td>
                                                <td>210.00 €</td>
                                                <td>26/05/2025</td>
                                                <td><span class="badge bg-warning text-dark">En cours</span></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="card h-100">
                            <div class="card-header">
                                <h5 class="mb-0">Répartition des frais</h5>
                            </div>
                            <div class="card-body">
                                <div class="chart-placeholder">
                                    <i class="bi bi-pie-chart"></i>
                                    <p>Graphique de répartition</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Toggle sidebar on mobile
            document.querySelector('.navbar-toggler').addEventListener('click', function() {
                document.getElementById('sidebar').classList.toggle('active');
            });

            // Initialize tooltips
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });

            console.log('Dashboard initialized');
        });
    </script>
</body>
</html>